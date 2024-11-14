package com.ingsis.parse.async

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ingsis.parse.format.FormatRequest
import com.ingsis.parse.format.FormatResponse
import com.ingsis.parse.validation.SnippetValidationResult

object JsonUtil {
  private val objectMapper: ObjectMapper = jacksonObjectMapper()

  fun serializeValidationRequest(snippet: SnippetValidationResult): String {
    return try {
      objectMapper.writeValueAsString(snippet)
    } catch (e: JsonProcessingException) {
      throw RuntimeException("Failed to serialize object to JSON", e)
    }
  }

  fun serializeFormatResponse(formatResponse: FormatResponse): String {
    return try {
      objectMapper.writeValueAsString(formatResponse)
    } catch (e: JsonProcessingException) {
      throw RuntimeException("Failed to serialize object to JSON", e)
    }
  }

  fun deserializeFormatRequest(json: String): FormatRequest {
    return try {
      objectMapper.readValue(json)
    } catch (e: JsonProcessingException) {
      throw RuntimeException("Failed to deserialize JSON to object", e)
    }
  }
}
