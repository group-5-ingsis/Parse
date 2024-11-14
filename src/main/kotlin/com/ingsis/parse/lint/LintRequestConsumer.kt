package com.ingsis.parse.lint

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
class LintRequestConsumer @Autowired constructor(
  redis: ReactiveRedisTemplate<String, String>,
  @Value("\${stream.lint-request}") streamRequestKey: String,
  @Value("\${groups.parser}") groupId: String,
  private val linterResponseProducer: LintResponseProducer,
  private val assetService: AssetService
) : RedisStreamConsumer<String>(streamRequestKey, groupId, redis) {

  override fun onMessage(record: ObjectRecord<String, String>) {
    val formatRequest = JsonUtil.deserializeLintRequest(record.value)
    val lintingRules = JsonUtil.deserializeLintingRules(assetService.getAssetContent(formatRequest.author, "LintingRules"))
    val result = PrintScript.lint(formatRequest.snippet, "1.1", lintingRules)

    val response = if (result.isEmpty()) {
      LintResponse(formatRequest.requestId, "compliant")
    } else {
      LintResponse(formatRequest.requestId, "non-compliant")
    }

    runBlocking {
      linterResponseProducer.publishEvent(JsonUtil.serializeLintResponse(response))
    }
  }

  override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, String>> {
    return StreamReceiver.StreamReceiverOptions.builder()
      .targetType(String::class.java)
      .build()
  }
}
