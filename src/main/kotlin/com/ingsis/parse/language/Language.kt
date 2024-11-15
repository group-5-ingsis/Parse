package com.ingsis.parse.language

import com.ingsis.parse.rules.LintRules
import rules.FormattingRules

interface Language {

  fun format(src: String, version: String, rules: FormattingRules): String

  fun lint(src: String, version: String, rules: LintRules): List<String>
}
