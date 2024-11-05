package com.ingsis.parse.async.consumer.format

import com.ingsis.parse.asset.Asset
import com.ingsis.parse.asset.AssetService
import com.ingsis.parse.async.JsonUtil
import com.ingsis.parse.language.LanguageProvider
import org.austral.ingsis.redis.RedisStreamConsumer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component

@Component
class FormatRequestConsumer @Autowired constructor(
  redis: ReactiveRedisTemplate<String, String>,
  @Value("\${stream.key}") streamKey: String,
  @Value("\${groups.format}") groupId: String,
  private val assetService: AssetService
) : RedisStreamConsumer<String>(streamKey, groupId, redis) {

  override fun onMessage(record: ObjectRecord<String, String>) {
    val streamValue = record.value

    val snippet = JsonUtil.deserializeFromJson(streamValue)
    val container = snippet.container
    val key = snippet.key
    val snippetLanguage = snippet.language
    val snippetVersion = snippet.version

    val snippetContent = assetService.getAssetContent(container, key)
    val language = LanguageProvider.getLanguages()[snippetLanguage]

    // Falta como agarrar las reglas de formateo del usuario!
    // val result = language?.format(snippetContent, snippetVersion)

    val asset = Asset(
      container,
      key,
      content = ""
      // content = result
    )

    assetService.updateAsset(asset)
  }

  override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, String>> {
    return StreamReceiver.StreamReceiverOptions.builder()
      .targetType(String::class.java)
      .build()
  }
}
