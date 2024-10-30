package com.ingsis.parse

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
open class Parse

fun main(args: Array<String>) {
  val dotenv = Dotenv.load()

  System.setProperty("SERVER_PORT", dotenv["SERVER_PORT"])
  System.setProperty("STREAM_KEY", dotenv["STREAM_KEY"])
  System.setProperty("GROUP_ID", dotenv["GROUP_ID"])

  runApplication<Parse>(*args)
}
