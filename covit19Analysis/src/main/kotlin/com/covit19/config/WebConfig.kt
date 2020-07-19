package com.covit19.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

// RestTemplate用の設定
@Configuration
class WebConfig {

  @Bean
  fun restTemplate(): RestTemplate {
    return RestTemplate()
  }
}