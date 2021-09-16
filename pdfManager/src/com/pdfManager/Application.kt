package com.pdfManager

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.pdfManager.controller.registerPdfController
import com.pdfManager.controller.userController
import com.pdfManager.model.jwt.AuthJwtPrincipal
import java.net.URI
import java.sql.Connection
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager

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
    val jwtAlgorithm = Algorithm.HMAC256(environment.config.property("jwt.secret").getString())
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtIssuer = environment.config.property("jwt.issuer").getString()
    val jwtProperty = environment.config.property("jwt.property").getString()
    install(Authentication) {
        jwt {
            realm = environment.config.property("jwt.realm").getString()
            verifier(
                JWT.require(jwtAlgorithm)
                    .withAudience(jwtAudience)
                    .withIssuer(jwtIssuer)
                    .build()
            )
            validate {
                it.payload.getClaim(jwtProperty).let { claim ->
                    if (!claim.isNull) {
                        AuthJwtPrincipal(claim.asString())
                    } else {
                        null
                    }
                }
            }
        }
    }
    install(Routing) {
        userController(environment)
        registerPdfController(environment)
    }
    Database.connect(
        url = environment.config.property("db.url").getString(),
        user = environment.config.property("db.user").getString(),
        password = environment.config.property("db.password").getString(),
        driver = environment.config.property("db.driver").getString()
    )
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_READ_COMMITTED
}

fun main(args: Array<String>) {
    embeddedServer(
        Netty,
        commandLineEnvironment(args)
    ).start()
}
