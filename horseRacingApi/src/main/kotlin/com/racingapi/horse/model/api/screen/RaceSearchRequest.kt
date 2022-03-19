package com.racingapi.horse.model.api.screen

import com.racingapi.horse.constants.NET_KEIBA_DOMAIN
import com.racingapi.horse.validator.checkUrlDomain
import org.valiktor.validate

data class RaceSearchRequest(val url: String = "") {
    init {
        validation()
    }

    private fun validation() {
        validate(this) {
            validate(RaceSearchRequest::url)
                .checkUrlDomain(NET_KEIBA_DOMAIN)
        }
    }
}
