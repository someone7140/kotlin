package com.pdfManager.controller

import com.pdfManager.model.api.auth.UserAuthRequest
import com.pdfManager.model.jwt.AuthJwtPrincipal
import com.pdfManager.useCase.authByAuthCode
import com.pdfManager.useCase.getAuthJwtInfoFromJson
import com.pdfManager.useCase.updateToken
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*


fun Route.userController(env: ApplicationEnvironment) {

    route("/user") {
        post("/auth") {
            var authCode = ""
            try {
                val userAuthRequest = call.receive<UserAuthRequest>()
                authCode = userAuthRequest.authCode
            } catch (ex: Exception) {
                throw BadRequestException("BadRequest")
            }
            try {
                val result = authByAuthCode(authCode, env)
                result
                    .onSuccess { res -> call.respond(HttpStatusCode.OK, res) }
                    .onFailure { call.respond(HttpStatusCode.Unauthorized) }
            } catch (ex: Exception) {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
        authenticate{
            post("/updateToken") {
                val principal = call.principal<AuthJwtPrincipal>()
                principal?.also {
                    // Jsonをデシリアライズ
                    val authJwtInfo = getAuthJwtInfoFromJson(it.authJson)
                    val result = updateToken(authJwtInfo, env)
                    result
                        .onSuccess { res -> call.respond(HttpStatusCode.OK, res) }
                        .onFailure { call.respond(HttpStatusCode.Unauthorized) }
                } ?: run {
                    call.respond(HttpStatusCode.Unauthorized)
                }
            }
        }
    }
}
