package com.ingsis.parse.parser

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/parse")
class ParseController(private val parseService: ParseService) {

  @PostMapping("/format/{snippetId}")
  fun formatSnippet(@PathVariable snippetId: String): String {
    TODO("NOT YET IMPLEMENTED")
  }

  @PostMapping("/snippets/format/all")
  fun formatAllSnippets(): ResponseEntity<String> {
    TODO("NOT YET IMPLEMENTED")
  }

  @PostMapping("/lint/{snippetId}")
  fun lintSnippet(@PathVariable snippetId: String): ResponseEntity<String> {
    TODO("NOT YET IMPLEMENTED")
  }

  @PostMapping("/all/lint")
  fun lintAllSnippets(): ResponseEntity<String> {
    TODO("NOT YET IMPLEMENTED")
  }
}
