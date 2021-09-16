package com.pdfManager.controller

import com.pdfManager.model.api.pdf.PdfAddRequest
import com.pdfManager.model.api.pdf.PdfDeleteRequest
import com.pdfManager.model.api.pdf.PdfUpdateRequest
import com.pdfManager.model.jwt.AuthJwtPrincipal
import com.pdfManager.useCase.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*


fun Route.registerPdfController(env: ApplicationEnvironment) {

    route("/pdf") {
        authenticate{
            post("/add") {
                try {
                    val req = call.receive<PdfAddRequest>()
                    val principal = call.principal<AuthJwtPrincipal>()
                    principal?.also {
                        // Jsonをデシリアライズ
                        val authJwtInfo = getAuthJwtInfoFromJson(it.authJson)
                        addPdfRegister(req.title, req.url, authJwtInfo.id)
                            .onSuccess { res -> call.respond(HttpStatusCode.OK, res) }
                            .onFailure { call.respond(HttpStatusCode.InternalServerError) }
                    } ?: run {
                        call.respond(HttpStatusCode.Unauthorized)
                    }
                } catch (ex: Exception) {
                    throw BadRequestException("BadRequest")
                }
            }
            post("/update") {
                try {
                    val req = call.receive<PdfUpdateRequest>()
                    val principal = call.principal<AuthJwtPrincipal>()
                    principal?.also {
                        // Jsonをデシリアライズ
                        val authJwtInfo = getAuthJwtInfoFromJson(it.authJson)
                        updatePdfRegister(req.id, req.title, req.url, authJwtInfo.id)
                            .onSuccess { res -> call.respond(HttpStatusCode.OK, res) }
                            .onFailure { call.respond(HttpStatusCode.InternalServerError) }
                    } ?: run {
                        call.respond(HttpStatusCode.Unauthorized)
                    }
                } catch (ex: Exception) {
                    throw BadRequestException("BadRequest")
                }
            }
            post("delete") {
                try {
                    val req = call.receive<PdfDeleteRequest>()
                    val principal = call.principal<AuthJwtPrincipal>()
                    principal?.also {
                        // Jsonをデシリアライズ
                        val authJwtInfo = getAuthJwtInfoFromJson(it.authJson)
                        deletePdfRegister(req.id, authJwtInfo.id)
                            .onSuccess { res -> call.respond(HttpStatusCode.OK, res) }
                            .onFailure { call.respond(HttpStatusCode.InternalServerError) }
                    } ?: run {
                        call.respond(HttpStatusCode.Unauthorized)
                    }
                } catch (ex: Exception) {
                    throw BadRequestException("BadRequest")
                }
            }
            get("/list") {
                val principal = call.principal<AuthJwtPrincipal>()
                principal?.also {
                    // Jsonをデシリアライズ
                    val authJwtInfo = getAuthJwtInfoFromJson(it.authJson)
                    getRegisterPdfs(authJwtInfo.id)
                        .onSuccess { res -> call.respond(HttpStatusCode.OK, res) }
                        .onFailure { call.respond(HttpStatusCode.InternalServerError) }
                } ?: run {
                    call.respond(HttpStatusCode.Unauthorized)
                }
            }
        }
    }
}
