package com.pdfManager.model.api.pdf

import com.pdfManager.validator.checkUrlFormat

import org.valiktor.functions.isNotBlank
import org.valiktor.validate

data class PdfUpdateRequest(
    val id: String = "",
    val title: String = "",
    val url: String = "") {
    init {
        validation()
    }

    private fun validation() {
        validate(this) {
            validate(PdfUpdateRequest::id)
                .isNotBlank()
            validate(PdfUpdateRequest::title)
                .isNotBlank()
            validate(PdfUpdateRequest::url)
                .checkUrlFormat()
        }
    }
}
