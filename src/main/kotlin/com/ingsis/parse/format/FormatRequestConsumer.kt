package com.ingsis.parse.format

import com.ingsis.parse.asset.AssetService
import com.ingsis.parse.async.JsonUtil
import com.ingsis.parse.language.PrintScript
import kotlinx.coroutines.runBlocking
import org.austral.ingsis.redis.RedisStreamConsumer
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
  private val formattedSnippetProducer: FormattedSnippetProducer,
  private val assetService: AssetService
) : RedisStreamConsumer<String>(streamRequestKey, groupId, redis) {

  override fun onMessage(record: ObjectRecord<String, String>) {
    val formatRequest = JsonUtil.deserializeFormatRequest(record.value)
    val formattingRules = assetService.getAssetContent(formatRequest.author, "FormattingRules")
    val result = PrintScript.format(formatRequest.snippet, "1.1", formattingRules)

    val response = FormatResponse(formatRequest.requestId, result)
    runBlocking {
      formattedSnippetProducer.publishEvent(JsonUtil.serializeFormatResponse(response))
    }
  }

  override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, String>> {
    return StreamReceiver.StreamReceiverOptions.builder()
      .targetType(String::class.java)
      .build()
  }
}
