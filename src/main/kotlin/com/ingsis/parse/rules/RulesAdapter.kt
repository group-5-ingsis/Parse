package com.ingsis.parse.rules
import kotlinx.serialization.json.Json
import rules.FormattingRules

object RulesAdapter {

  fun toPrintScript(config: String): FormattingRules {
    val config = Json.decodeFromString<FormatRules>(config)

    return FormattingRules(
      spaceBeforeColon = config.spaceBeforeColon,
      spaceAfterColon = config.spaceAfterColon,
      spaceAroundAssignment = config.spaceAroundAssignment,
      newlineAfterPrintln = config.newlineAfterPrintln,
      blockIndentation = config.blockIndentation,
      ifBraceSameLine = config.ifBraceSameLine,
      singleSpaceSeparation = config.singleSpaceSeparation
    )
  }
}
