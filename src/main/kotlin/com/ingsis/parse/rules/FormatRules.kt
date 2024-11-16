package com.ingsis.parse.rules

import com.fasterxml.jackson.annotation.JsonProperty

data class FormatRules(
  @JsonProperty("spaceBeforeColon") val spaceBeforeColon: Boolean,
  @JsonProperty("spaceAfterColon") val spaceAfterColon: Boolean,
  @JsonProperty("spaceAroundAssignment") val spaceAroundAssignment: Boolean,
  @JsonProperty("newlineAfterPrintln") val newlineAfterPrintln: Int,
  @JsonProperty("blockIndentation") val blockIndentation: Int,
  @JsonProperty("if-brace-same-line") val ifBraceSameLine: Boolean
) : Rule
