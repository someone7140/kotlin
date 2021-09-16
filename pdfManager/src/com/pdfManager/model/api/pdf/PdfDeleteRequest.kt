package com.pdfManager.model.api.pdf

import org.valiktor.functions.isNotBlank
import org.valiktor.validate

data class PdfDeleteRequest(val id: String = "") {
    init {
        validation()
    }

    private fun validation() {
        validate(this) {
            validate(PdfDeleteRequest::id)
                .isNotBlank()
        }
    }
}
