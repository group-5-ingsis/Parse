package com.ingsis.parse.language

import com.ingsis.parse.rules.FormatRules
import com.ingsis.parse.rules.LintRules
import com.ingsis.parse.rules.RuleAdapter
import formatter.Formatter
import lexer.Lexer
import linter.Linter
import parser.Parser
import java.io.IOException

object PrintScript : Language {
  override fun format(
    src: String,
    version: String,
    rules: FormatRules
  ): String {
    val printScriptRules = RuleAdapter.toPrintScriptFormattingRules(rules)

    val output = StringBuilder()

    try {
      val tokens = Lexer(src, version)
      val asts = Parser(tokens, version, null)
      val formattedFile = Formatter.format(asts, printScriptRules, version)
      output.append(formattedFile)
    } catch (e: IOException) {
      System.err.println("I/O Error: ${e.message}")
    } catch (e: Exception) {
      System.err.println("Error: ${e.message}")
    } finally {
      try {
      } catch (e: IOException) {
        System.err.println("Error closing writer: ${e.message}")
      }
    }
    return output.toString()
  }

  override fun lint(
    src: String,
    version: String,
    rules: LintRules
  ): List<String> {
    val printScriptRules = RuleAdapter.toPrintScriptLinterRules(rules)
    val tokens = Lexer(src, version)

    val errors = mutableListOf<String>()
    val asts = Parser(tokens, version, null)

    val linter = Linter(printScriptRules)

    while (asts.hasNext()) {
      try {
        val result = linter.lint(asts)
        if (!result.isValid()) {
          errors.add(result.getMessage())
        }
      } catch (e: Exception) {
        errors.add("Error during linting: ${e.message}")
      } catch (e: Error) {
        errors.add("Critical error during linting: ${e.message}")
      }
    }
    return errors
  }
}
