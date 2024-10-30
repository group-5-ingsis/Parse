package com.ingsis.parse.health

import com.ingsis.parse.redis.producer.OperationResultProducer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping()
class HealthController(private val producer: OperationResultProducer) {

  @GetMapping("/info")
  fun getServiceInfo(): ResponseEntity<String> {
    val serviceInfo = "Service Name: Parse Service\nVersion: 1.0.0"
    return ResponseEntity(serviceInfo, HttpStatus.OK)
  }

  @PostMapping("/redisTest")
  suspend fun redisTest() {
    producer.publishEvent("1", "format", "OK")
  }
}
