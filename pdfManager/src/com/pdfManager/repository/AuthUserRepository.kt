package com.pdfManager.repository

import com.pdfManager.model.db.AuthUsers
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun updateAuthUer(id: String, authType: String, refreshToken: String, refreshTokenExpireTime: Long) {
    transaction {
        AuthUsers.select { AuthUsers.id.eq(id) and AuthUsers.authType.eq(authType) }
            .limit(1).firstOrNull()?.also {
                AuthUsers.update(
                    { AuthUsers.id.eq(it[AuthUsers.id]) and AuthUsers.authType.eq(it[AuthUsers.authType]) }
                ) {
                    it[AuthUsers.refreshToken] = refreshToken
                    it[AuthUsers.refreshTokenExpireTime] = refreshTokenExpireTime
                }
            } ?: run {
              AuthUsers.insert {
                it[AuthUsers.id] = id
                it[AuthUsers.authType] = authType
                it[AuthUsers.refreshToken] = refreshToken
                it[AuthUsers.refreshTokenExpireTime] = refreshTokenExpireTime
            }
        }
    }
}

fun getRefreshToken(id: String, authType: String): Pair<String, Long>? {
    var result: Pair<String, Long>? = null
    transaction {
        AuthUsers.select { AuthUsers.id.eq(id) and AuthUsers.authType.eq(authType) }
            .limit(1).firstOrNull()?.also {
                result = Pair(it[AuthUsers.refreshToken], it[AuthUsers.refreshTokenExpireTime])
            }
    }
    return result
}
