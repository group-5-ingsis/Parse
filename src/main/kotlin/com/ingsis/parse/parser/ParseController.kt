package com.ingsis.parse.parser

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/parse")
class ParseController(private val parseService: ParseService) {

  @PostMapping("/{snippetId}/format")
  fun formatSnippet(@PathVariable("snippetId") snippetId: String): ResponseEntity<String> {
    TODO("NOT YET IMPLEMENTED")
  }

  @PostMapping("/all/format")
  fun formatAllSnippets(): ResponseEntity<String> {
    TODO("NOT YET IMPLEMENTED")
  }

  @PostMapping("/{snippetId}/lint")
  fun lintSnippet(@PathVariable("snippetId") snippetId: String): ResponseEntity<String> {
    TODO("NOT YET IMPLEMENTED")
  }

  @PostMapping("/all/lint")
  fun lintAllSnippets(): ResponseEntity<String> {
    TODO("NOT YET IMPLEMENTED")
  }
}
