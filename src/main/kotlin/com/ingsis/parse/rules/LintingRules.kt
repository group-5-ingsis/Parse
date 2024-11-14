package com.ingsis.parse.rules

import kotlinx.serialization.Serializable
import rules.LinterRules

@Serializable
data class LintingRules(
  val version: String = "1.1",
  val identifierNamingConvention: String,
  val printlnExpressionAllowed: Boolean,
  val readInputExpressionAllowed: Boolean
) : LinterRules {

  override fun getAsMap(): Map<String, Any> {
    val rulesMap = mutableMapOf<String, Any>(
      "identifierNamingConvention" to identifierNamingConvention,
      "printlnExpressionAllowed" to printlnExpressionAllowed
    )

    if (version >= "1.1") {
      rulesMap["readInputExpressionAllowed"] = readInputExpressionAllowed
    }

    return rulesMap
  }
}
