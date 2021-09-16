package com.pdfManager.model.jwt

data class AuthJwtInfo(
    val id: String,
    val name: String,
    val accessToken: String,
    val accessTokenExpireTime: Long,
)
