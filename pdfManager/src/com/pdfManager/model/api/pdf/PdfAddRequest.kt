package com.pdfManager.model.api.pdf

import com.pdfManager.validator.checkUrlFormat

import org.valiktor.functions.isNotBlank
import org.valiktor.validate

data class PdfAddRequest(val title: String = "", val url: String = "") {
    init {
        validation()
    }

    private fun validation() {
        validate(this) {
            validate(PdfAddRequest::title)
                .isNotBlank()
            validate(PdfAddRequest::url)
                .checkUrlFormat()
        }
    }
}
