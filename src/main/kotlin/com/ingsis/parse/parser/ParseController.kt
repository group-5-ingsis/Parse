// package com.ingsis.parse.parser
//
// import com.ingsis.parse.async.producer.OperationResultProducer
// import org.springframework.http.ResponseEntity
// import org.springframework.web.bind.annotation.PathVariable
// import org.springframework.web.bind.annotation.PostMapping
// import org.springframework.web.bind.annotation.RequestBody
// import org.springframework.web.bind.annotation.RestController
//
// class ParseController(private val parseService: ParseService, private val producer: OperationResultProducer) {
//
// //  @PostMapping("/format")
// //  fun formatSnippet(@RequestBody snippet): String {
// //    return parseService.formatSnippet(snippetId)
// //  }
//
//  @PostMapping("/format/all")
//  fun formatAllSnippets(): ResponseEntity<String> {
//    TODO("NOT YET IMPLEMENTED")
//  }
//
//  @PostMapping("/lint/{snippetId}")
//  fun lintSnippet(@PathVariable snippetId: String): ResponseEntity<String> {
//    TODO("NOT YET IMPLEMENTED")
//  }
//
//  @PostMapping("lint/all")
//  fun lintAllSnippets(): ResponseEntity<String> {
//    TODO("NOT YET IMPLEMENTED")
//  }
//
//  @PostMapping("/redisTest")
//  suspend fun redisTest() {
//    producer.publishEvent("1", "format", "OK")
//  }
//
// }
