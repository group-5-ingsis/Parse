package com.ingsis.parse.language

import com.ingsis.parse.rules.FormatRules
import com.ingsis.parse.rules.RulesAdapter
import formatter.Formatter
import lexer.Lexer
import parser.Parser
import java.io.IOException

object PrintScript : Language {
  override fun format(
    src: String,
    version: String,
    rules: FormatRules
  ): String {
    val printScriptFormattingRules = RulesAdapter.toPrintScript(rules)

    val output = StringBuilder()

    try {
      val tokens = Lexer(src, version)
      val asts = Parser(tokens, version, null)
      val formattedFile = Formatter.format(asts, printScriptFormattingRules, version)
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

  override fun validate(src: String, version: String, input: String): Boolean {
    var valid = true

    try {
      val tokens = Lexer(src, version)
      Parser(tokens, version, input)
    } catch (e: IOException) {
      System.err.println("I/O Error: ${e.message}")
    } catch (e: Exception) {
      valid = false
      System.err.println("Error: ${e.message}")
    } finally {
      try {
      } catch (e: IOException) {
        System.err.println("Error closing writer: ${e.message}")
      }
    }
    return valid
  }
}