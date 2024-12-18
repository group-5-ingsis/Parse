package com.ingsis.parse.config

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ingsis.parse.async.FormatRequest
import com.ingsis.parse.async.FormatResponse
import com.ingsis.parse.async.LintRequest
import com.ingsis.parse.async.LintResponse
import com.ingsis.parse.rules.FormatRules
import com.ingsis.parse.rules.LintRules
import com.ingsis.parse.rules.Rule

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

  fun deserializeFormattingRules(json: String): FormatRules {
    return try {
      objectMapper.readValue(json)
    } catch (e: JsonProcessingException) {
      throw RuntimeException("Failed to deserialize JSON to object", e)
    }
  }

  fun deserializeLintingRules(json: String): LintRules {
    return try {
      objectMapper.readValue(json)
    } catch (e: JsonProcessingException) {
      throw RuntimeException("Failed to deserialize JSON to object", e)
    }
  }

  fun serializeRules(rules: Rule): String {
    return try {
      objectMapper.writeValueAsString(rules)
    } catch (e: JsonProcessingException) {
      throw RuntimeException("Failed to serialize object to JSON", e)
    }
  }
}
