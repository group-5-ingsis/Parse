package com.ingsis.parse.async.validation

import com.ingsis.parse.async.JsonUtil
import kotlinx.coroutines.reactive.awaitSingle
import org.austral.ingsis.redis.RedisStreamProducer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component

@Component
class ValidationRequestProducer @Autowired constructor(
  @Value("\${stream.validate}") streamKey: String,
  redis: ReactiveRedisTemplate<String, String>
) : RedisStreamProducer(streamKey, redis) {

  suspend fun publishEvent(snippet: SnippetValidationResult) {
    val snippetAsJson = JsonUtil.serializeValidationRequest(snippet)
    emit(snippetAsJson).awaitSingle()
  }
}
