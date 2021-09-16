package com.pdfManager.model.db

import org.jetbrains.exposed.sql.*

object RegisterPdfs: Table("register_pdfs") {
    val id = varchar("id", 100).primaryKey()
    val title = varchar("title", 1000)
    val url = varchar("url", 1000)
    val type = varchar("type", 100)
    val userId = varchar("user_id", 100)
    val registerTime = long("register_time")
}
