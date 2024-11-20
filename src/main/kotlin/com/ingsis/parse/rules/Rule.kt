package com.ingsis.parse.rules

import com.fasterxml.jackson.annotation.JsonProperty

sealed interface Rule

data class FormatRules(
  @JsonProperty("spaceBeforeColon") val spaceBeforeColon: Boolean,
  @JsonProperty("spaceAfterColon") val spaceAfterColon: Boolean,
  @JsonProperty("spaceAroundAssignment") val spaceAroundAssignment: Boolean,
  @JsonProperty("newlineAfterPrintln") val newlineAfterPrintln: Int,
  @JsonProperty("blockIndentation") val blockIndentation: Int,
  @JsonProperty("if-brace-same-line") val ifBraceSameLine: Boolean
) : Rule {
  companion object {
    fun asDefault(): FormatRules {
      return FormatRules(
        spaceBeforeColon = false,
        spaceAfterColon = false,
        spaceAroundAssignment = false,
        newlineAfterPrintln = 0,
        blockIndentation = 0,
        ifBraceSameLine = false
      )
    }

    const val KEY = "FormattingRules"
  }
}

data class LintRules(
  @JsonProperty("identifierNamingConvention") val identifierNamingConvention: String,
  @JsonProperty("printlnExpressionAllowed") val printlnExpressionAllowed: Boolean,
  @JsonProperty("readInputExpressionAllowed") val readInputExpressionAllowed: Boolean
) : Rule {
  companion object {
    fun asDefault(): LintRules {
      return LintRules(
        identifierNamingConvention = "snake-case",
        printlnExpressionAllowed = false,
        readInputExpressionAllowed = false
      )
    }

    const val KEY = "LintingRules"
  }
}
