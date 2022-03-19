package com.racingapi.horse

import com.racingapi.horse.controller.raceAnalyticsController
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.routing.*
import java.net.URI

fun Application.module() {
    val frontEndUri = URI(environment.config.property("serverInfo.frontendUrl").getString())
    install(CORS) {
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Options)
        host(frontEndUri.authority, schemes = listOf(frontEndUri.scheme))
        allowCredentials = true
        allowNonSimpleContentTypes = true
        header(HttpHeaders.Authorization)
    }
    install(ContentNegotiation) {
        jackson {}
    }
    install(Routing) {
        raceAnalyticsController(environment)
    }
}

fun main(args: Array<String>) {
    embeddedServer(
        Netty,
        commandLineEnvironment(args)
    ).start()
}
