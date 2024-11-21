package com.ingsis.parse.lint

import LanguageProvider
import com.ingsis.parse.asset.AssetService
import com.ingsis.parse.async.LintRequest
import com.ingsis.parse.async.LintResponse
import com.ingsis.parse.config.JsonUtil
import com.ingsis.parse.language.Language
import com.ingsis.parse.rules.LintRules
import com.ingsis.parse.rules.RuleManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import org.austral.ingsis.redis.RedisStreamConsumer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class LintRequestConsumer @Autowired constructor(
  redis: ReactiveRedisTemplate<String, String>,
  @Value("\${stream.lint-request}") streamRequestKey: String,
  @Value("\${groups.parser}") groupId: String,
  private val linterResponseProducer: LintResponseProducer,
  private val assetService: AssetService
) : RedisStreamConsumer<String>(streamRequestKey, groupId, redis) {

  private val logger = LoggerFactory.getLogger(LintRequestConsumer::class.java)

  init {
    logger.info("LintRequestConsumer initialized with stream key: $streamRequestKey and group ID: $groupId")
  }

  override fun onMessage(record: ObjectRecord<String, String>) {
    val lintRequest = deserializeLintRequest(record.value) ?: return
    logger.info("Received request to lint snippet with requestId: ${lintRequest.requestId}")
    processLintRequest(lintRequest)
  }

  private fun deserializeLintRequest(recordValue: String): LintRequest? {
    return try {
      JsonUtil.deserializeLintRequest(recordValue)
    } catch (e: Exception) {
      logger.error("Failed to deserialize lint request: $recordValue", e)
      null
    }
  }

  private fun processLintRequest(lintRequest: LintRequest) {
    try {
      val lintingRules = fetchLintingRules(lintRequest)

      val language = LanguageProvider.getLanguage(lintRequest.language)
      val result = lintSnippet(language, lintRequest, lintingRules)

      val response = createLintResponse(lintRequest, result)

      publishLintResponse(response)
    } catch (e: Exception) {
      logger.error("Error processing lint request for requestId ${lintRequest.requestId}: ${e.message}", e)
    }
  }

  private fun fetchLintingRules(lintRequest: LintRequest): LintRules {
    val ruleJson = RuleManager.getLintingRules(lintRequest.author, LintRules.KEY, assetService)
    return JsonUtil.deserializeLintingRules(ruleJson)
  }

  private fun lintSnippet(language: Language, lintRequest: LintRequest, lintingRules: LintRules): List<String> {
    val version = getLanguageVersion(lintRequest, language)
    return language.lint(lintRequest.snippet, version, lintingRules)
  }

  private fun getLanguageVersion(
    lintRequest: LintRequest,
    language: Language
  ): String {
    val snippetLanguage = lintRequest.language
    val parts = snippetLanguage.split(" ", limit = 2)
    if (parts.size != 2) {
      throw IllegalArgumentException("Invalid language format. Expected format: 'LanguageName version'. Got: $language")
    }

    val version = parts[1]
    return version
  }

  private fun createLintResponse(lintRequest: LintRequest, result: List<String>): LintResponse {
    val status = if (result.isEmpty()) "compliant" else "non-compliant"
    return LintResponse(lintRequest.requestId, status)
  }

  private fun publishLintResponse(response: LintResponse) {
    CoroutineScope(Dispatchers.IO).launch {
      val timeoutMillis = 5000L
      val success = withTimeoutOrNull(timeoutMillis) {
        try {
          linterResponseProducer.publishEvent(JsonUtil.serializeLintResponse(response))
          logger.info("Published lint response to producer for requestId ${response.requestId}")
        } catch (e: Exception) {
          logger.error("Error publishing lint response for requestId ${response.requestId}: ${e.message}", e)
        }
      }
      if (success == null) {
        logger.error("Timed out while publishing lint response for requestId ${response.requestId}")
      }
    }
  }

  override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, String>> {
    logger.debug("Configuring StreamReceiver options for LintRequestConsumer")
    return StreamReceiver.StreamReceiverOptions.builder()
      .targetType(String::class.java)
      .pollTimeout(Duration.ofSeconds(5))
      .build()
  }
}
