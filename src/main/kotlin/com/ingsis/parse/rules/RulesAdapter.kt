package com.ingsis.parse.rules
import rules.FormattingRules

object RulesAdapter {

  fun toPrintScript(config: FormatRules): FormattingRules {
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
