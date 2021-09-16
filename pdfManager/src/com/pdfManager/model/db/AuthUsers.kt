package com.pdfManager.model.db

import org.jetbrains.exposed.sql.*

object AuthUsers: Table("auth_users") {
    val id = varchar("id", 100).primaryKey()
    val authType = varchar("auth_type", 255).primaryKey()
    val refreshToken = varchar("refresh_token", 1000)
    val refreshTokenExpireTime = long("refresh_token_expire_time")
}
