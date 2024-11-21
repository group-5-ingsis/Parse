package com.ingsis.parse.async

data class FormatRequest(
  val requestId: String,
  val author: String,
  val snippet: String
)

data class LintRequest(
  val requestId: String,
  val author: String,
  val snippet: String,
  val language: String
)
