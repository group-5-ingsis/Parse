package com.ingsis.parse.format

import com.ingsis.parse.asset.AssetService
import com.ingsis.parse.async.JsonUtil
import com.ingsis.parse.language.PrintScript
import com.ingsis.parse.rules.FormatRules
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
class FormatRequestConsumer @Autowired constructor(
  redis: ReactiveRedisTemplate<String, String>,
  @Value("\${stream.format}") streamRequestKey: String,
  @Value("\${groups.parser}") groupId: String,
  private val formatResponseProducer: FormatResponseProducer,
  private val assetService: AssetService
) : RedisStreamConsumer<String>(streamRequestKey, groupId, redis) {

  private val logger = LoggerFactory.getLogger(FormatRequestConsumer::class.java)

  init {
    logger.info("FormatRequestConsumer initialized with stream key: $streamRequestKey and group ID: $groupId")
  }

  override fun onMessage(record: ObjectRecord<String, String>) {
    val formatRequest = try {
      JsonUtil.deserializeFormatRequest(record.value)
    } catch (e: Exception) {
      logger.error("Failed to deserialize format request: ${record.value}", e)
      return
    }

    logger.info("Received request to format snippet with requestId: ${formatRequest.requestId}")

    try {
      logger.debug("Deserialized format request: {}", formatRequest)

      val rulesJson = try {
        RuleManager.getRules(formatRequest.author, FormatRules.KEY, assetService)
      } catch (e: Exception) {
        logger.error("Failed to fetch formatting rules for author ${formatRequest.author}: ${e.message}", e)
        return
      }

      logger.debug("Fetched formatting rules JSON for author ${formatRequest.author}")

      val formattingRules = try {
        JsonUtil.deserializeFormattingRules(rulesJson)
      } catch (e: Exception) {
        logger.error("Failed to deserialize formatting rules for author ${formatRequest.author}: ${e.message}", e)
        return
      }

      logger.debug("Deserialized formatting rules: {}", formattingRules)

      val result = PrintScript.format(formatRequest.snippet, "1.1", formattingRules)
      logger.info("Finished formatting snippet with requestId: ${formatRequest.requestId}")

      val response = FormatResponse(formatRequest.requestId, result)

      CoroutineScope(Dispatchers.IO).launch {
        val timeoutMillis = 5000L

        val success = withTimeoutOrNull(timeoutMillis) {
          try {
            formatResponseProducer.publishEvent(JsonUtil.serializeFormatResponse(response))
            logger.info("Published format result for requestId: ${formatRequest.requestId}")
          } catch (e: Exception) {
            logger.error("Error publishing format result for requestId ${formatRequest.requestId}: ${e.message}", e)
          }
        }

        if (success == null) {
          logger.error("Timed out while publishing format result for requestId ${formatRequest.requestId}")
        }
      }
    } catch (e: Exception) {
      logger.error("Error processing format request for requestId ${formatRequest.requestId}: ${e.message}", e)
    }
  }

  override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, String>> {
    logger.debug("Configuring StreamReceiver options for FormatRequestConsumer")
    return StreamReceiver.StreamReceiverOptions.builder()
      .targetType(String::class.java)
      .pollTimeout(Duration.ofSeconds(5))
      .build()
  }
}
