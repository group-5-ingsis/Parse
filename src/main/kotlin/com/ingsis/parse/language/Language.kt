package com.ingsis.parse.language

interface Language {

  fun format(src: String, version: String, rules: String): String

  fun validate(src: String, version: String, input: String): Boolean
}
