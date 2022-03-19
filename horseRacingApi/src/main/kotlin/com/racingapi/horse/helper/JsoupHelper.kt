package com.racingapi.horse.helper

import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

const val TIME_OUT_MILLI_SEC = 90 * 1000

fun getHtmlDoc(url: String): Document? {
    return runCatching {
        Jsoup.connect(url).ignoreHttpErrors(true).timeout(TIME_OUT_MILLI_SEC).get()
    }.fold(
        onSuccess = { it },
        onFailure = {
            null
        }
    )
}

fun getHtmlDocWithForm(url: String, formData: HashMap<String, String>): Document? {
    return runCatching {
        Jsoup.connect(url)
            .method(Connection.Method.POST)
            .data(formData)
            .ignoreHttpErrors(true)
            .timeout(TIME_OUT_MILLI_SEC)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .postDataCharset("EUC-JP")
            .execute()
            .parse()
    }.fold(
        onSuccess = { it },
        onFailure = {
            null
        }
    )
}