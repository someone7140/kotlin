package com.pdfManager.model.jwt

import io.ktor.auth.*

data class AuthJwtPrincipal(val authJson: String) : Principal
