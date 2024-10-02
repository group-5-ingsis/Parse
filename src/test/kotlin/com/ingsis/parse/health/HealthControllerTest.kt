package com.ingsis.parse.health

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import kotlin.test.Test

@WebMvcTest
class HealthControllerTest {

  @Autowired
  lateinit var mockMvc: MockMvc

  @Test
  fun `checkHealth should return OK status with a message`() {
    mockMvc.get("/health")
      .andExpect {
        status { isOk() }
        content { string("Service is running") }
      }
  }

  @Test
  fun `sayHello should return OK status with Hello, World! message`() {
    mockMvc.get("/health/hello")
      .andExpect {
        status { isOk() }
        content { string("Hello, World!") }
      }
  }
}
