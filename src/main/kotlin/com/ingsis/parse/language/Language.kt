package com.ingsis.parse.language

import com.ingsis.parse.rules.FormatRules

interface Language {

  fun format(src: String, version: String, rules: FormatRules)
}
