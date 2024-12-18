package com.ingsis.parse.rules

import com.ingsis.parse.asset.Asset
import com.ingsis.parse.asset.AssetService
import com.ingsis.parse.config.JsonUtil

object RuleManager {

  fun getFormattingRules(username: String, type: String, assetService: AssetService): String {
    val rulesJson = assetService.getAssetContent(username, type)
    return if (rulesJson == "Error retrieving asset content: 404 Not Found: [no body]") {
      val rules = FormatRules.asDefault()
      saveRules(username, type, rules, assetService)
      assetService.getAssetContent(username, type)
    } else {
      rulesJson
    }
  }

  fun getLintingRules(username: String, type: String, assetService: AssetService): String {
    val rulesJson = assetService.getAssetContent(username, type)
    return if (rulesJson == "Error retrieving asset content: 404 Not Found: [no body]") {
      val rules = LintRules.asDefault()
      saveRules(username, type, rules, assetService)
      assetService.getAssetContent(username, type)
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
}
