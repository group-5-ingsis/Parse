package com.ingsis.parse.asset

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Service
class AssetService(private val restTemplate: RestTemplate) {

  private val assetServiceBaseUrl: String = System.getProperty("ASSET_SERVICE_URL")

  fun getAssetContent(container: String, key: String): String {
    val headers = createHeaders(MediaType.TEXT_EVENT_STREAM)
    val url = buildUrl(container, key)

    return try {
      val response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity<Unit>(null, headers), String::class.java)
      response.body ?: "No Content"
    } catch (e: RestClientException) {
      handleException(e, "Error retrieving asset content")
    }
  }

  private fun createHeaders(contentType: MediaType? = null): HttpHeaders {
    return HttpHeaders().apply {
      contentType?.let { this.contentType = it }
      accept = listOf(MediaType.ALL)
    }
  }

  private fun buildUrl(container: String, key: String): String {
    return "$assetServiceBaseUrl/$container/$key"
  }

  private fun handleException(e: RestClientException, defaultMessage: String): String {
    return "$defaultMessage: ${e.message ?: "Unknown error"}"
  }
}
