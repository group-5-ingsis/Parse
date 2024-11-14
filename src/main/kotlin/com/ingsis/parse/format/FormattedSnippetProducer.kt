package com.ingsis.parse.format

import kotlinx.coroutines.reactive.awaitSingle
import org.austral.ingsis.redis.RedisStreamProducer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component

@Component
class FormattedSnippetProducer @Autowired constructor(
  @Value("\${stream.format-response}") streamResponseKey: String,
  redis: ReactiveRedisTemplate<String, String>
) : RedisStreamProducer(streamResponseKey, redis) {

  suspend fun publishEvent(response: String) {
    emit(response).awaitSingle()
  }
}
