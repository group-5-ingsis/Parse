package com.ingsis.parse.rules

data class LintRules(
  val identifierNamingConvention: String,
  val printlnExpressionAllowed: Boolean,
  val readInputExpressionAllowed: Boolean
) : Rule
