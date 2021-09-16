package com.pdfManager.model.domain.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.api.client.auth.oauth2.BearerToken
import com.google.api.client.auth.oauth2.ClientParametersAuthentication
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.auth.oauth2.TokenResponse
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.oauth2.Oauth2
import com.pdfManager.model.jwt.AuthJwtInfo
import com.pdfManager.repository.getRefreshToken
import com.pdfManager.repository.updateAuthUer
import io.ktor.application.*
import java.util.*


data class AuthInfo(
    val env: ApplicationEnvironment
) {
    var id = String()
    var name = String()
    var jwtToken = String()
    var accessTokenExpireTime = 0L
    var accessToken = String()
    private var refreshToken = String()
    private var refreshTokenExpireTime = 0L
    private var authCode = String()

    // 認証コードを取得している場合
    constructor(authCodeInput: String, env: ApplicationEnvironment) : this(env) {
        authCode = authCodeInput
        if (authCode.isNotEmpty()) {
            // 認証コードから認証情報を取得してセット
            setAuthInfoFromAuthCode(
                googleClientId = env.config.property("google.clientId").getString(),
                googleSecret = env.config.property("google.secret").getString(),
                backendUrl = env.config.property("serverInfo.backendUrl").getString()
            )
            if (id.isNotEmpty() && accessToken.isNotEmpty() && refreshToken.isNotEmpty()) {
                // アクセストークンの期限取得
                accessTokenExpireTime = getNewAccessTokenExpireTime()
                // リフレッシュトークンの期限取得
                refreshTokenExpireTime = getNewRefreshTokenExpireTime()
                // Jwtの設定
                jwtToken = getJwtToken(
                    env,
                    ObjectMapper().writeValueAsString(
                        AuthJwtInfo(id, name, accessToken, accessTokenExpireTime)
                    )
                )
                // DBのレコード更新
                updateUserRecord()
            }
        }
    }

    // すでに一度認証している場合
    constructor(authJwtInfo: AuthJwtInfo, env: ApplicationEnvironment) : this(env) {
        if (authJwtInfo.id.isNotEmpty()) {
            id = authJwtInfo.id
            getRefreshToken(id, "google")?.also {
                refreshToken = it.first
                refreshTokenExpireTime = it.second
                setAuthInfoFromRefreshToken(
                    googleClientId = env.config.property("google.clientId").getString(),
                    googleSecret = env.config.property("google.secret").getString(),
                    backendUrl = env.config.property("serverInfo.backendUrl").getString()
                )
                if (accessToken.isNotEmpty()) {
                    // アクセストークンの期限取得
                    accessTokenExpireTime = getNewAccessTokenExpireTime()
                    // Jwtの設定
                    jwtToken = getJwtToken(
                        env,
                        ObjectMapper().writeValueAsString(
                            AuthJwtInfo(id, name, accessToken, accessTokenExpireTime)
                        )
                    )
                }
            }
        }
    }

    // 認証コードからトークンを取得してセット
    private fun setAuthInfoFromAuthCode(googleClientId: String, googleSecret: String, backendUrl: String) {
        val req = GoogleAuthorizationCodeTokenRequest(
            NetHttpTransport(),
            JacksonFactory.getDefaultInstance(),
            "https://oauth2.googleapis.com/token",
            googleClientId,
            googleSecret,
            authCode,
            "postmessage"
        )
        req.grantType = "authorization_code"
        val tokenResponse = req.execute()
        accessToken = tokenResponse.accessToken
        refreshToken = tokenResponse.refreshToken
        // プロファイル情報を取得してセット
        setProfileFromToken(tokenResponse, googleClientId, googleSecret, backendUrl)
    }

    // リグレッシュトークンからトークンを取得してセット
    private fun setAuthInfoFromRefreshToken(googleClientId: String, googleSecret: String, backendUrl: String) {
        val tokenResponse = GoogleRefreshTokenRequest(
            NetHttpTransport(),
            JacksonFactory.getDefaultInstance(),
            refreshToken,
            googleClientId,
            googleSecret
        ).execute()
        accessToken = tokenResponse.accessToken
        setProfileFromToken(tokenResponse, googleClientId, googleSecret, backendUrl)
    }

    // トークンからプロファイルを取得してセット
    private fun setProfileFromToken(
        token: TokenResponse, googleClientId: String, googleSecret: String, backendUrl: String
    ) {
        val credential = Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
            .setTransport(NetHttpTransport())
            .setJsonFactory(JacksonFactory.getDefaultInstance())
            .setClientAuthentication(ClientParametersAuthentication(googleClientId, googleSecret))
            .setTokenServerUrl(GenericUrl(backendUrl))
            .build()
            .setFromTokenResponse(token)
        val oauth2 =
            Oauth2.Builder(NetHttpTransport(), JacksonFactory(), credential)
                .setApplicationName("Oauth2")
                .build()
        val userinfo = oauth2.userinfo().get().execute()
        id = userinfo.id
        name = userinfo.name
    }

    // アクセストークンの期限を新規に取得
    private fun getNewAccessTokenExpireTime(): Long {
        return System.currentTimeMillis() + 60L * 60L * 1000L
    }

    // リフレッシュトークンの期限を新規に取得
    private fun getNewRefreshTokenExpireTime(): Long {
        return System.currentTimeMillis() + 60L * 60L * 24L * 30L * 1000L
    }

    // Jwtトークンの取得
    private fun getJwtToken(env: ApplicationEnvironment, claim: String): String {
        return JWT.create()
            .withAudience(env.config.property("jwt.audience").getString())
            .withExpiresAt(Date(refreshTokenExpireTime))
            .withClaim(env.config.property("jwt.property").getString(), claim)
            .withIssuer(env.config.property("jwt.issuer").getString())
            .sign(Algorithm.HMAC256(env.config.property("jwt.secret").getString()))
    }

    // DBのユーザレコードの更新
    private fun updateUserRecord() {
        updateAuthUer(id, "google", refreshToken, refreshTokenExpireTime)
    }

}
