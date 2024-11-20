package com.ingsis.parse.parse

import com.ingsis.parse.language.PrintScript
import com.ingsis.parse.rules.FormatRules
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
class ParseE2ETests {

  @Test
  fun `should format code correctly with valid rules`() {
    val src = "let x: number=5;"
    val version = "1.1"

    val rules = FormatRules(
      spaceBeforeColon = false,
      spaceAfterColon = true,
      spaceAroundAssignment = true,
      newlineAfterPrintln = 2,
      blockIndentation = 2,
      ifBraceSameLine = false
    )

    val formattedCode = PrintScript.format(src, version, rules)

    val expected = "let x: number = 5;\n"
    assertEquals(expected, formattedCode)
  }
}
