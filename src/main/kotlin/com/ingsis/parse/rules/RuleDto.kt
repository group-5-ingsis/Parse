package com.ingsis.parse.rules

data class RuleDto(
  val id: String,
  val name: String,
  val isActive: Boolean,
  val value: Any? = null
)
