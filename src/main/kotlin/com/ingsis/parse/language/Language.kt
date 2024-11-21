package com.ingsis.parse.language

import com.ingsis.parse.rules.FormatRules
import com.ingsis.parse.rules.LintRules

interface Language {

  fun format(src: String, version: String, rules: FormatRules): String

  fun lint(src: String, version: String, rules: LintRules): List<String>
}
