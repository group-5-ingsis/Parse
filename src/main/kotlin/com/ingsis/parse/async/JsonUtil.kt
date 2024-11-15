package com.ingsis.parse.async

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ingsis.parse.format.FormatRequest
import com.ingsis.parse.format.FormatResponse
import com.ingsis.parse.lint.LintRequest
import com.ingsis.parse.lint.LintResponse
import com.ingsis.parse.rules.LintingRules
import rules.FormattingRules

object JsonUtil {
  private val objectMapper: ObjectMapper = jacksonObjectMapper()

  fun serializeFormatResponse(formatResponse: FormatResponse): String {
    return try {
      objectMapper.writeValueAsString(formatResponse)
    } catch (e: JsonProcessingException) {
      throw RuntimeException("Failed to serialize object to JSON", e)
    }
  }

  fun serializeLintResponse(formatResponse: LintResponse): String {
    return try {
      objectMapper.writeValueAsString(formatResponse)
    } catch (e: JsonProcessingException) {
      throw RuntimeException("Failed to serialize object to JSON", e)
    }
  }

  fun serializeFormattingRules(formatResponse: FormattingRules): String {
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

  fun deserializeLintRequest(json: String): LintRequest {
    return try {
      objectMapper.readValue(json)
    } catch (e: JsonProcessingException) {
      throw RuntimeException("Failed to deserialize JSON to object", e)
    }
  }

  fun deserializeFormattingRules(json: String): FormattingRules {
    return try {
      objectMapper.readValue(json)
    } catch (e: JsonProcessingException) {
      throw RuntimeException("Failed to deserialize JSON to object", e)
    }
  }

  fun deserializeLintingRules(json: String): LintingRules {
    return try {
      objectMapper.readValue(json)
    } catch (e: JsonProcessingException) {
      throw RuntimeException("Failed to deserialize JSON to object", e)
    }
  }
}
