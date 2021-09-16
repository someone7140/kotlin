package com.pdfManager.useCase

import com.pdfManager.model.api.pdf.PdfListResponse
import com.pdfManager.model.db.RegisterPdfs
import com.pdfManager.model.domain.pdf.RegisterPdfInfo
import com.pdfManager.repository.getRegisterPdfsByUser

fun addPdfRegister(title: String, url: String, userId: String): Result<Boolean> {
    try {
        val pdf = RegisterPdfInfo(title, url, userId)
        val result = pdf.addPdfInfo()
        if (result) {
            return Result.success(true)
        }
        return Result.failure(Exception("Failed add pdf"))
    } catch (ex: Exception) {
        return Result.failure(ex)
    }
}

fun getRegisterPdfs(userId: String): Result<List<PdfListResponse>> {
    return try {
        val resultRows = getRegisterPdfsByUser(userId)
        Result.success(
            resultRows.map {
                PdfListResponse(
                    it[RegisterPdfs.id],
                    it[RegisterPdfs.title],
                    it[RegisterPdfs.url]
                )
            }
        )
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}

fun updatePdfRegister(id: String, title: String, url: String, userId: String): Result<Boolean> {
    return try {
        val pdf = RegisterPdfInfo(id, title, url, userId)
        val result = pdf.updatePdfInfo()
        if (result) {
            return Result.success(true)
        }
        return Result.failure(Exception("Failed update pdf"))
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}

fun deletePdfRegister(id: String, userId: String): Result<Boolean> {
    return try {
        val pdf = RegisterPdfInfo(id, userId)
        val result = pdf.deletePdfInfo()
        if (result) {
            return Result.success(true)
        }
        return Result.failure(Exception("Failed delete pdf"))
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}
