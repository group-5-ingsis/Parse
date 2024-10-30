package com.ingsis.parse.redis.producer

data class OperationResult(
  var snippetId: String,
  var operation: String,
  var result: String
)
