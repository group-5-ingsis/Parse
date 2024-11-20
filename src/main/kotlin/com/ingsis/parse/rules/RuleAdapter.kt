package com.ingsis.parse.rules

import rules.FormattingRules
import rules.LinterRules
import rules.LinterRulesV2

object RuleAdapter {

  fun toPrintScriptLinterRules(rules: LintRules): LinterRules {
    return LinterRulesV2(
      identifierNamingConvention = rules.identifierNamingConvention,
      printlnExpressionAllowed = rules.printlnExpressionAllowed,
      readInputExpressionAllowed = rules.readInputExpressionAllowed
    )
  }

  fun toPrintScriptFormattingRules(rules: FormatRules): FormattingRules {
    return FormattingRules(
      spaceBeforeColon = rules.spaceBeforeColon,
      spaceAfterColon = rules.spaceAfterColon,
      spaceAroundAssignment = rules.spaceAroundAssignment,
      newlineAfterPrintln = rules.newlineAfterPrintln,
      blockIndentation = rules.blockIndentation,
      ifBraceSameLine = rules.ifBraceSameLine,
      singleSpaceSeparation = true
    )
  }
}
