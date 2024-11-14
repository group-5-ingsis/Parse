// package com.ingsis.parse.validation
//
// import com.ingsis.parse.asset.AssetService
// import com.ingsis.parse.async.JsonUtil
// import com.ingsis.parse.language.LanguageProvider
// import kotlinx.coroutines.CoroutineScope
// import kotlinx.coroutines.Dispatchers
// import kotlinx.coroutines.launch
// import org.austral.ingsis.redis.RedisStreamConsumer
// import org.springframework.beans.factory.annotation.Autowired
// import org.springframework.beans.factory.annotation.Value
// import org.springframework.data.redis.connection.stream.ObjectRecord
// import org.springframework.data.redis.core.ReactiveRedisTemplate
// import org.springframework.data.redis.stream.StreamReceiver
// import org.springframework.stereotype.Component
//
// @Component
// class ValidationRequestConsumer @Autowired constructor(
//  redis: ReactiveRedisTemplate<String, String>,
//  @Value("\${stream.validate}") streamKey: String,
//  @Value("\${groups.parser}") groupId: String,
//  private val assetService: AssetService,
//  private val validationRequestProducer: ValidationRequestProducer
// ) : RedisStreamConsumer<String>(streamKey, groupId, redis) {
//
//  override fun onMessage(record: ObjectRecord<String, String>) {
//    CoroutineScope(Dispatchers.IO).launch {
//      processMessage(record)
//    }
//  }
//
//  private suspend fun processMessage(record: ObjectRecord<String, String>) {
//    val streamValue = record.value
//    val snippet = JsonUtil.deserializeFormatRequest(streamValue)
//
//    val container = snippet.container
//    val key = snippet.key
//    val snippetLanguage = snippet.language
//    val snippetVersion = snippet.version
//
//    val snippetContent = assetService.getAssetContent(container, key)
//    val language = LanguageProvider.getLanguages()[snippetLanguage]
//
//    val isValid = language?.validate(snippetContent, snippetVersion, "null")
//    if (isValid == false) {
//      val result = SnippetValidationResult(
//        snippetId = key,
//        result = "invalid"
//      )
//      validationRequestProducer.publishEvent(result)
//    }
//  }
//
//  override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, String>> {
//    return StreamReceiver.StreamReceiverOptions.builder()
//      .targetType(String::class.java)
//      .build()
//  }
// }
