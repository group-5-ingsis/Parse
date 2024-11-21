package com.ingsis.parse.async

data class FormatResponse(
  val requestId: String,
  val content: String
)

data class LintResponse(
  val requestId: String,
  val status: String
)
