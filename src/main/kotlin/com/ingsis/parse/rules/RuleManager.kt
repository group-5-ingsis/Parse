package com.ingsis.parse.rules

import com.ingsis.parse.asset.Asset
import com.ingsis.parse.asset.AssetService
import com.ingsis.parse.async.JsonUtil
import rules.FormattingRules

object RuleManager {

  private fun getDefaultFormattingRules(): FormatRules {
    return FormatRules(
      spaceBeforeColon = false,
      spaceAfterColon = false,
      spaceAroundAssignment = false,
      newlineAfterPrintln = 0,
      blockIndentation = 0,
      ifBraceSameLine = false,
      singleSpaceSeparation = false
    )
  }

  private fun getDefaultLintingRules(): LintRules {
    return LintRules(
      identifierNamingConvention = "snake-case",
      printlnExpressionAllowed = false,
      readInputExpressionAllowed = false
    )
  }

  fun getLintingRulesJson(username: String, assetService: AssetService): String {
    val lintingRules = "LintingRules"
    val rulesJson = assetService.getAssetContent(username, lintingRules)
    return if (rulesJson == "Error retrieving asset content: 404 Not Found: [no body]") {
      val defaultRules = getDefaultLintingRules()
      saveRules(username, lintingRules, defaultRules, assetService)
      assetService.getAssetContent(username, lintingRules)
    } else {
      rulesJson
    }
  }

  fun getFormattingRulesJson(username: String, assetService: AssetService): String {
    val formattingRules = "FormattingRules"
    val rulesJson = assetService.getAssetContent(username, formattingRules)
    return if (rulesJson == "Error retrieving asset content: 404 Not Found: [no body]") {
      val defaultRules = getDefaultFormattingRules()
      saveRules(username, formattingRules, defaultRules, assetService)
      assetService.getAssetContent(username, formattingRules)
    } else {
      rulesJson
    }
  }

  private fun saveRules(username: String, key: String, rules: Any, assetService: AssetService) {
    val rulesAsJson = when (rules) {
      is FormatRules -> JsonUtil.serializeRules(rules)
      is LintRules -> JsonUtil.serializeRules(rules)
      else -> throw IllegalArgumentException("Unsupported rules type")
    }
    val asset = Asset(container = username, key = key, content = rulesAsJson)
    assetService.createOrUpdateAsset(asset)
  }

  fun adaptPrintScriptLintRules(rules: LintRules): LintingRules {
    return LintingRules(
      version = "1.1",
      identifierNamingConvention = rules.identifierNamingConvention,
      printlnExpressionAllowed = rules.printlnExpressionAllowed,
      readInputExpressionAllowed = rules.readInputExpressionAllowed
    )
  }

  fun adaptPrintScriptFormatRules(rules: FormatRules): FormattingRules {
    return FormattingRules(
      spaceBeforeColon = rules.spaceBeforeColon,
      spaceAfterColon = rules.spaceAfterColon,
      spaceAroundAssignment = rules.spaceAroundAssignment,
      newlineAfterPrintln = rules.newlineAfterPrintln,
      blockIndentation = rules.blockIndentation,
      ifBraceSameLine = rules.ifBraceSameLine,
      singleSpaceSeparation = rules.singleSpaceSeparation
    )
  }
}
