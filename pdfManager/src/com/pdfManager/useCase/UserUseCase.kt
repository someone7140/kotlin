package com.pdfManager.useCase

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.pdfManager.model.api.auth.UserAuthResponse
import com.pdfManager.model.domain.auth.AuthInfo
import com.pdfManager.model.jwt.AuthJwtInfo
import io.ktor.application.*
import org.apache.http.auth.AuthenticationException

fun getAuthJwtInfoFromJson(json: String): AuthJwtInfo {
    val mapper = ObjectMapper().registerKotlinModule()
    return mapper.readValue(json, AuthJwtInfo::class.java)
}

fun authByAuthCode(authCode: String, env: ApplicationEnvironment): Result<UserAuthResponse> {
    return getUserAuthResponse(AuthInfo(authCode, env))
}

fun updateToken(authJwtInfo: AuthJwtInfo, env: ApplicationEnvironment): Result<UserAuthResponse> {
    return getUserAuthResponse(AuthInfo(authJwtInfo, env))
}

private fun getUserAuthResponse(authInfo: AuthInfo): Result<UserAuthResponse> {
    if (authInfo.jwtToken.isNotEmpty()) {
        return Result.success(
            UserAuthResponse(authInfo.name, authInfo.jwtToken, authInfo.accessTokenExpireTime)
        )
    }
    return Result.failure(AuthenticationException())
}
