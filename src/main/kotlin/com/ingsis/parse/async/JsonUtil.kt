package com.ingsis.parse.async

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

object JsonUtil {
  private val objectMapper: ObjectMapper = jacksonObjectMapper()

  fun serializeToJson(snippet: Snippet): String {
    return try {
      objectMapper.writeValueAsString(snippet)
    } catch (e: JsonProcessingException) {
      throw RuntimeException("Failed to serialize object to JSON", e)
    }
  }

  fun deserializeFromJson(json: String): Snippet {
    return try {
      objectMapper.readValue(json)
    } catch (e: JsonProcessingException) {
      throw RuntimeException("Failed to deserialize JSON to object", e)
    }
  }
}
