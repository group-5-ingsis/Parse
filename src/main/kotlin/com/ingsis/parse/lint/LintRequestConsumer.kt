package com.ingsis.parse.lint

import com.ingsis.parse.asset.AssetService
import com.ingsis.parse.config.JsonUtil
import com.ingsis.parse.language.PrintScript
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
    val lintRequest = try {
      JsonUtil.deserializeLintRequest(record.value)
    } catch (e: Exception) {
      logger.error("Failed to deserialize lint request: ${record.value}", e)
      return
    }

    logger.info("Received request to lint snippet with requestId: ${lintRequest.requestId}")

    try {
      logger.debug("Deserialized lint request: {}", lintRequest)

      val ruleJson = try {
        RuleManager.getLintingRules(lintRequest.author, LintRules.KEY, assetService)
      } catch (e: Exception) {
        logger.error("Failed to fetch linting rules for author ${lintRequest.author}: ${e.message}", e)
        return
      }

      logger.debug("Fetched linting rules JSON for author ${lintRequest.author}")

      val lintingRules = try {
        JsonUtil.deserializeLintingRules(ruleJson)
      } catch (e: Exception) {
        logger.error("Failed to deserialize linting rules for author ${lintRequest.author}: ${e.message}", e)
        return
      }

      logger.debug("Deserialized linting rules: {}", lintingRules)

      val result = PrintScript.lint(lintRequest.snippet, "1.1", lintingRules)
      logger.info("Lint result for requestId ${lintRequest.requestId}: $result")

      val response = if (result.isEmpty()) {
        LintResponse(lintRequest.requestId, "compliant")
      } else {
        LintResponse(lintRequest.requestId, "non-compliant")
      }

      CoroutineScope(Dispatchers.IO).launch {
        val timeoutMillis = 5000L

        val success = withTimeoutOrNull(timeoutMillis) {
          try {
            linterResponseProducer.publishEvent(JsonUtil.serializeLintResponse(response))
            logger.info("Published lint response to producer for requestId ${lintRequest.requestId}")
          } catch (e: Exception) {
            logger.error("Error publishing lint response for requestId ${lintRequest.requestId}: ${e.message}", e)
          }
        }

        if (success == null) {
          logger.error("Timed out while publishing lint response for requestId ${lintRequest.requestId}")
        }
      }
    } catch (e: Exception) {
      logger.error("Error processing lint request for requestId ${lintRequest.requestId}: ${e.message}", e)
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
