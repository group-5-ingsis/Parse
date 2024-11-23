package com.ingsis.parse.parse

import com.ingsis.parse.asset.AssetService
import com.ingsis.parse.language.LanguageProvider
import com.ingsis.parse.language.PrintScript
import com.ingsis.parse.rules.FormatRules
import com.ingsis.parse.rules.LintRules
import com.ingsis.parse.rules.RuleAdapter
import com.ingsis.parse.rules.RuleManager
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
class ParseE2ETests {

  private val assetService: AssetService = mock()

  @Test
  fun `test getFormattingRules returns default rules when asset is not found`() {
    val username = "testUser"
    val type = FormatRules.KEY
    val errorResponse = "Error retrieving asset content: 404 Not Found: [no body]"

    whenever(assetService.getAssetContent(username, type)).thenReturn(errorResponse)

    val result = RuleManager.getFormattingRules(username, type, assetService)

    assertNotNull(result)
  }

  @Test
  fun `test getLintingRules returns default rules when asset is not found`() {
    val username = "testUser"
    val type = FormatRules.KEY
    val errorResponse = "Error retrieving asset content: 404 Not Found: [no body]"

    whenever(assetService.getAssetContent(username, type)).thenReturn(errorResponse)

    val result = RuleManager.getLintingRules(username, type, assetService)

    assertNotNull(result)
  }

  @Test
  fun `should format code correctly with valid rules`() {
    val src = "let x: number=5;"
    val version = "1.1"

    val rules = FormatRules.asDefault()

    val formattedCode = PrintScript.format(src, version, rules)

    val expected = "let x:number=5;\n"
    assertEquals(expected, formattedCode)
  }

  @Test
  fun `should not format code correctly with valid rules`() {
    val src = "let x: number=5 asdasdasdasd;"
    val version = "1.1"

    val rules = FormatRules.asDefault()

    val formattedCode = PrintScript.format(src, version, rules)

    val expected = ""
    assertEquals(expected, formattedCode)
  }

  @Test
  fun `should lint code correctly with valid rules`() {
    val src = "let x: number=5;"
    val version = "1.1"

    val rules = LintRules.asDefault()

    val result = PrintScript.lint(src, version, rules)

    val expected = listOf<String>()
    assertEquals(expected, result)
  }

  @Test
  fun `should lint code correctly with incorrect rules`() {
    val src = "println(2 + 2);"
    val version = "1.1"

    val rules = LintRules.asDefault()

    val result = PrintScript.lint(src, version, rules)

    val expected = listOf<String>()
    assertEquals(expected, result)
  }

  @Test
  fun `test toPrintScriptFormattingRules`() {
    val formatRules = FormatRules(
      spaceBeforeColon = true,
      spaceAfterColon = true,
      spaceAroundAssignment = true,
      newlineAfterPrintln = 2,
      blockIndentation = 4,
      ifBraceSameLine = false
    )

    val result = RuleAdapter.toPrintScriptFormattingRules(formatRules)

    assertEquals(true, result.spaceBeforeColon)
    assertEquals(true, result.spaceAfterColon)
    assertEquals(true, result.spaceAroundAssignment)
    assertEquals(2, result.newlineAfterPrintln)
    assertEquals(4, result.blockIndentation)
    assertEquals(false, result.ifBraceSameLine)
    assertEquals(true, result.singleSpaceSeparation)
  }

  @Test
  fun `test toPrintScriptLinterRules`() {
    val lintRules = LintRules(
      identifierNamingConvention = "camelCase",
      printlnExpressionAllowed = true,
      readInputExpressionAllowed = false
    )

    val result = RuleAdapter.toPrintScriptLinterRules(lintRules)

    val a = result.getAsMap()
    assertEquals("camelCase", a["identifierNamingConvention"])
    assertEquals(true, a["printlnExpressionAllowed"])
    assertEquals(false, a["readInputExpressionAllowed"])
  }

  @Test
  fun getLanguagesTest() {
    val expected = PrintScript
    val actual = LanguageProvider.getLanguage("printScript")
    assertEquals(expected, actual)
  }
}
