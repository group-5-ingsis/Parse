package com.ingsis.parse.parser

import org.springframework.web.bind.annotation.RestController

@RestController("/parse")
class ParseController(private val parseService: ParseService) {
  
}
