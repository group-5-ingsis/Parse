package com.ingsis.parse.config

import com.ingsis.parse.logging.CorrelationIdInterceptor
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
open class RestTemplateConfig(private val correlationIdInterceptor: CorrelationIdInterceptor) {

  @Bean
  open fun restTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate {
    return restTemplateBuilder
      .additionalInterceptors(correlationIdInterceptor)
      .build()
  }
}
