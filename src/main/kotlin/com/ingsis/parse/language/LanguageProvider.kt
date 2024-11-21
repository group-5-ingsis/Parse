import com.ingsis.parse.language.Language
import com.ingsis.parse.language.PrintScript

object LanguageProvider {

  private val languageMap: Map<String, Language> = mapOf(
    "printScript" to PrintScript
  )

  fun getLanguage(language: String): Language {
    return languageMap[language] ?: PrintScript
  }
}
