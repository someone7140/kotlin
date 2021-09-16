package com.pdfManager.model.api.auth

data class UserAuthResponse(
    val name: String = "",
    val jwtToken: String = "",
    val accessTokenExpireTime: Long = 0L
)
