package com.racingapi.horse.controller

import com.racingapi.horse.model.api.screen.RaceSearchRequest
import com.racingapi.horse.useCase.analyticsRace
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.raceAnalyticsController(env: ApplicationEnvironment) {

    route("/race") {
        post("/analytics") {
            try {
                val req = call.receive<RaceSearchRequest>()

                try {
                    analyticsRace(req.url)?.also {
                        if (it.isNotEmpty()) {
                            call.respond(HttpStatusCode.OK, it)
                        } else {
                            // 空配列の場合はサイトの解析に失敗している
                            throw InternalError("ScrapingError")
                        }
                    } ?: run {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                } catch (ex: Exception) {
                    throw InternalError("InternalError")
                }
            } catch (ex: Exception) {
                throw BadRequestException("BadRequest")
            }
        }
    }

}
