package com.ingsis.parse.redis.producer

import com.ingsis.parse.redis.JsonUtil
import kotlinx.coroutines.reactive.awaitSingle
import org.austral.ingsis.redis.RedisStreamProducer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component

@Component
class OperationResultProducer @Autowired constructor(
  @Value("\${stream.key}") streamKey: String,
  redis: ReactiveRedisTemplate<String, String>
) : RedisStreamProducer(streamKey, redis) {

  suspend fun publishEvent(snippetId: String, operation: String, result: String) {
    val operationResult = OperationResult(snippetId, operation, result)
    val jsonMessage = JsonUtil.serializeToJson(operationResult)
    emit(jsonMessage).awaitSingle()
  }
}
