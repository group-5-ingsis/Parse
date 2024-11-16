package com.ingsis.parse.format

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
class FormatRequestConsumer @Autowired constructor(
  redis: ReactiveRedisTemplate<String, String>,
  @Value("\${stream.format}") streamRequestKey: String,
  @Value("\${groups.parser}") groupId: String,
  private val formatResponseProducer: FormatResponseProducer,
  private val assetService: AssetService
) : RedisStreamConsumer<String>(streamRequestKey, groupId, redis) {

  private val logger = LoggerFactory.getLogger(FormatRequestConsumer::class.java)

  override fun onMessage(record: ObjectRecord<String, String>) {
    val formatRequest = JsonUtil.deserializeFormatRequest(record.value)
    logger.info("Received request to format snippet with request: ${formatRequest.requestId}")
    val rulesJson = RuleManager.getFormattingRulesJson(formatRequest.author, assetService)

    val formattingRules = JsonUtil.deserializeFormattingRules(rulesJson)

    val result = PrintScript.format(formatRequest.snippet, "1.1", formattingRules)
    logger.info("Finished formatting snippet with request: ${formatRequest.requestId}")

    val response = FormatResponse(formatRequest.requestId, result)
    runBlocking {
      formatResponseProducer.publishEvent(JsonUtil.serializeFormatResponse(response))
      logger.info("Published format result with request: ${formatRequest.requestId}")
    }
  }

  override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, String>> {
    return StreamReceiver.StreamReceiverOptions.builder()
      .targetType(String::class.java)
      .build()
  }
}
