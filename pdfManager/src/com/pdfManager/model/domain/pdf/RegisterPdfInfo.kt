package com.pdfManager.model.domain.pdf

import com.pdfManager.repository.addRegisterPdf
import com.pdfManager.repository.deleteRegisterPdf
import com.pdfManager.repository.updateRegisterPdf
import java.net.URI
import java.net.URL
import java.util.*


data class RegisterPdfInfo(
    val title: String, val url: String, val userId: String
) {

    private val GOOGLE_DRIVE_DOMAIN = "docs.google.com"

    var id = String()
    var type = String()
    var registerTime = 0L

    // idを含めてインスタンス化
    constructor(id: String, title: String, url: String, userId: String) : this(title, url, userId) {
        this.id = id;
    }

    // id・userIdのみでインスタンス化
    constructor(id: String, userId: String) : this(String(), String(), userId) {
        this.id = id;
    }

    // 追加
    fun addPdfInfo(): Boolean {
        try {
            // idの生成
            val uuid = UUID.randomUUID()
            id = uuid.toString()
            // typeの判定
            val uri = URL(url).toURI()
            type = getPdfTypeFromUri(uri)
            // 登録日の取得
            registerTime = System.currentTimeMillis()

            // insert
            addPdfRecord()

            return true
        } catch (ex: Exception) {
            return false
        }
        return true
    }

    // 更新
    fun updatePdfInfo(): Boolean {
        return return try {
            // typeの取得
            val uri = URL(url).toURI()
            type = getPdfTypeFromUri(uri)
            // update
            val count = updatePdfRecord()
            count > 0
        } catch (ex: Exception) {
            false
        }
    }

    // 削除
    fun deletePdfInfo(): Boolean {
        return return try {
            // delete
            val count = deletePdfRecord()
            count > 0
        } catch (ex: Exception) {
            false
        }
    }

    // DBのPDFレコードの追加
    private fun addPdfRecord() {
        addRegisterPdf(id, title, url, type, userId, registerTime)
    }

    // DBのPDFレコード更新
    private fun updatePdfRecord(): Int {
        return updateRegisterPdf(id, title, url, type, userId)
    }

    // DBのPDFレコード削除
    private fun deletePdfRecord(): Int {
        return deleteRegisterPdf(id, userId)
    }

    private fun getPdfTypeFromUri(uri: URI): String {
        return if (uri.host.equals(GOOGLE_DRIVE_DOMAIN)) "googleDrive" else "url"
    }
}
