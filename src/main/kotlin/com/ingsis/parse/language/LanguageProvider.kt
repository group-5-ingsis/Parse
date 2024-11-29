package com.ingsis.parse.language

object LanguageProvider {

  private val languageMap: Map<String, Language> = mapOf(
    "printScript" to PrintScript
  )

  fun getLanguage(language: String): Language {
    return languageMap[language] ?: PrintScript
  }
}
