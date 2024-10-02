package com.ingsis.parse.health

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/health")
class HealthController {

  @GetMapping
  fun checkHealth(): ResponseEntity<String> {
    return ResponseEntity("Service is running", HttpStatus.OK)
  }

  @GetMapping("/hello")
  fun sayHello(): ResponseEntity<String> {
    return ResponseEntity("Hello, World!", HttpStatus.OK)
  }
}
