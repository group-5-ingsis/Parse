package com.ingsis.parse.language

object LanguageProvider {

  fun getLanguages(): Map<String, Language> {
    val map = mutableMapOf<String, Language>()
    map.put("PrintScript", PrintScript)
    return map
  }
}
