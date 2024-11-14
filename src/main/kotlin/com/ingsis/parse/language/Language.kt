package com.ingsis.parse.language

import rules.FormattingRules

interface Language {

  fun format(src: String, version: String, rules: FormattingRules): String

  fun validate(src: String, version: String, input: String): Boolean
}
