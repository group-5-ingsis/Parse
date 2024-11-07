package com.ingsis.parse

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
open class Parse

fun main(args: Array<String>) {
  val dotenv = Dotenv.load()

  val profile = dotenv["SPRING_PROFILES_ACTIVE"] ?: "local"
  System.setProperty("spring.profiles.active", profile)

  if (profile == "local") {
    System.setProperty("SERVER_PORT", dotenv["SERVER_PORT"])
    System.setProperty("STREAM_KEY_FORMAT", dotenv["STREAM_KEY_FORMAT"])
    System.setProperty("STREAM_KEY_VALIDATE", dotenv["STREAM_KEY_VALIDATE"])
    System.setProperty("GROUP_ID", dotenv["GROUP_ID"])
    System.setProperty("ASSET_SERVICE_URL", dotenv["ASSET_SERVICE_URL"])
    System.setProperty("REDIS_HOST", dotenv["REDIS_HOST"])
  }

  runApplication<Parse>(*args)
}
