package com.ingsis.parse.lint

import com.ingsis.parse.asset.AssetService
import com.ingsis.parse.async.JsonUtil
import com.ingsis.parse.language.PrintScript
import com.ingsis.parse.rules.RuleManager
import kotlinx.coroutines.runBlocking
import org.austral.ingsis.redis.RedisStreamConsumer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component

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
    val formatRequest = JsonUtil.deserializeLintRequest(record.value)
    logger.info("Received request to lint snippet with requestId: ${formatRequest.requestId}")

    try {
      logger.debug("Deserialized format request: {}", formatRequest)
      val ruleJson = RuleManager.getLintingRulesJson(formatRequest.author, assetService)
      logger.debug("Fetched linting rules JSON for author ${formatRequest.author}")

      val lintingRules = JsonUtil.deserializeLintingRules(ruleJson)
      logger.debug("Deserialized linting rules: {}", lintingRules)

      val result = PrintScript.lint(formatRequest.snippet, "1.1", lintingRules)
      logger.info("Lint result for requestId ${formatRequest.requestId}: $result")

      val response = if (result.isEmpty()) {
        LintResponse(formatRequest.requestId, "compliant")
      } else {
        LintResponse(formatRequest.requestId, "non-compliant")
      }

      logger.info("Sending lint response for requestId ${formatRequest.requestId} with status: ${response.status}")

      runBlocking {
        linterResponseProducer.publishEvent(JsonUtil.serializeLintResponse(response))
        logger.info("Published lint response to producer for requestId ${formatRequest.requestId}")
      }
    } catch (e: Exception) {
      logger.error("Error processing lint request for requestId ${formatRequest.requestId}: ${e.message}", e)
    }
  }

  override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, String>> {
    logger.debug("Configuring StreamReceiver options for LintRequestConsumer")
    return StreamReceiver.StreamReceiverOptions.builder()
      .targetType(String::class.java)
      .build()
  }
}
