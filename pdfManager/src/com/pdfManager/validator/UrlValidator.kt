package com.pdfManager.validator

import org.valiktor.Constraint
import org.valiktor.Validator
import java.net.URL

class UrlValidator : Constraint

fun <E> Validator<E>.Property<String?>.checkUrlFormat() =
    this.validate(UrlValidator()) {
        if (it != null) {
            try {
                URL(it.toString()).toURI();
            } catch (ex: Exception) {
                return@validate false
            }
            return@validate true
        } else {
            return@validate false
        }
    }
