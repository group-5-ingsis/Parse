package com.ingsis.parse.logging

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.FilterConfig
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.MDC
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CorrelationIdFilter : Filter {

  companion object {
    const val CORRELATION_ID_HEADER = "X-Correlation-ID"
  }

  override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
    val httpServletRequest = request as HttpServletRequest
    val correlationId = httpServletRequest.getHeader(CORRELATION_ID_HEADER) ?: UUID.randomUUID().toString()
    MDC.put(CORRELATION_ID_HEADER, correlationId)

    try {
      chain.doFilter(request, response)
    } finally {
      MDC.remove(CORRELATION_ID_HEADER)
    }
  }

  override fun init(filterConfig: FilterConfig?) {}
  override fun destroy() {}
}
