package com.ingsis.parse.rules

import kotlinx.serialization.Serializable

@Serializable
data class FormatRules(
  val spaceBeforeColon: Boolean,
  val spaceAfterColon: Boolean,
  val spaceAroundAssignment: Boolean,
  val newlineAfterPrintln: Int,
  val blockIndentation: Int,
  val ifBraceSameLine: Boolean,
  val singleSpaceSeparation: Boolean
)
