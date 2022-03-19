package com.racingapi.horse.validator

import org.valiktor.Constraint
import org.valiktor.Validator
import java.net.URL

class UrlValidator : Constraint

fun <E> Validator<E>.Property<String?>.checkUrlDomain(domain: String) =
    this.validate(UrlValidator()) {
        if (it != null) {
            try {
                val url = URL(it.toString()).toURI();
                return@validate url.host.endsWith(domain)
            } catch (ex: Exception) {
                return@validate false
            }
            return@validate true
        } else {
            return@validate false
        }
    }
