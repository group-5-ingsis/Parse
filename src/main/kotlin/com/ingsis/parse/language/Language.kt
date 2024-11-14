package com.ingsis.parse.language

import com.ingsis.parse.rules.LintingRules
import rules.FormattingRules

interface Language {

  fun format(src: String, version: String, rules: FormattingRules): String

  fun validate(src: String, version: String, input: String): Boolean

  fun lint(src: String, version: String, rules: LintingRules): String
}
