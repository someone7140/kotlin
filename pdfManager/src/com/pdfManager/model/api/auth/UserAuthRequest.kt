package com.pdfManager.model.api.auth

import org.valiktor.functions.isNotBlank
import org.valiktor.validate

data class UserAuthRequest(val authCode: String = "") {
    init {
        validation()
    }

    private fun validation() {
        validate(this) {
            validate(UserAuthRequest::authCode)
                .isNotBlank()
        }
    }
}
