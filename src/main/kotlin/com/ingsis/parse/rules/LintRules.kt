package com.ingsis.parse.rules

import com.fasterxml.jackson.annotation.JsonProperty

data class LintRules(
  @JsonProperty("identifierNamingConvention") val identifierNamingConvention: String,
  @JsonProperty("printlnExpressionAllowed") val printlnExpressionAllowed: Boolean,
  @JsonProperty("readInputExpressionAllowed") val readInputExpressionAllowed: Boolean
) : Rule
