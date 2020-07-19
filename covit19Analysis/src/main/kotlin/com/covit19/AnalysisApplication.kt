package com.covit19

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder



@SpringBootApplication
@EnableScheduling
class AnalysisApplication

fun main(args: Array<String>) {
  runApplication<AnalysisApplication>(*args)
}


