package com.pdfManager.repository

import com.pdfManager.model.db.RegisterPdfs
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun addRegisterPdf(
    id: String,
    title: String,
    url: String,
    type: String,
    userId: String,
    registerTime: Long
) {
    transaction {
        RegisterPdfs.insert {
            it[RegisterPdfs.id] = id
            it[RegisterPdfs.title] = title
            it[RegisterPdfs.url] = url
            it[RegisterPdfs.type] = type
            it[RegisterPdfs.userId] = userId
            it[RegisterPdfs.registerTime] = registerTime
        }
    }
}

fun getRegisterPdfsByUser(userId: String): List<ResultRow> {
    return transaction {
        RegisterPdfs.select(RegisterPdfs.userId.eq(userId)).orderBy(RegisterPdfs.registerTime, SortOrder.DESC).toList()
    }
}

fun updateRegisterPdf(
    id: String,
    title: String,
    url: String,
    type: String,
    userId: String
): Int {
    return transaction {
        RegisterPdfs.update({ RegisterPdfs.id.eq(id) and RegisterPdfs.userId.eq(userId) }) {
            it[RegisterPdfs.title] = title
            it[RegisterPdfs.url] = url
            it[RegisterPdfs.type] = type
        }
    }
}

fun deleteRegisterPdf(id: String, userId: String): Int {
    return transaction {
        RegisterPdfs.deleteWhere { RegisterPdfs.id.eq(id) and RegisterPdfs.userId.eq(userId) }
    }
}
