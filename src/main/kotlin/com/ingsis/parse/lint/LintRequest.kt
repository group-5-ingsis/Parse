package com.ingsis.parse.lint

data class LintRequest(
  val requestId: String,
  val author: String,
  val snippet: String
)
