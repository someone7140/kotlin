package com.racingapi.horse.helper

import org.jsoup.nodes.Element

private const val MINUTE_UNIT = 60L * 1000L;
private const val SEC_UNIT = 1000L;
private const val MILL_SEC_UNIT = 100L;

// HTMLの競馬場要素から文字列を取得
fun getLocationFromElement(elem: Element): String {
    return elem.ownText().trim().replace(Regex("[0-9]"), "")
}

// HTMLの年齢・性別要素から文字列を取得
fun getAgeAndGenderFromElement(elem: Element): Pair<Int?, String?> {
    val ageAndGender = elem.ownText().trim()
    val age = ageAndGender.substring(1, ageAndGender.length).toIntOrNull()
    val gender = ageAndGender.substring(0, 1)
    return Pair(age, gender)
}

// HTMLのタイム要素からミリ秒に変換した結果を取得
fun getTimeFromElement(elem: Element): Long {
    return elem.ownText().trim().split(":", ".").withIndex().mapNotNull {
        (it.value.toLongOrNull() ?: 0L) * (when (it.index) {
            0 -> MINUTE_UNIT
            1 -> SEC_UNIT
            else -> MILL_SEC_UNIT
        })
    }.sum()
}

// ミリ秒から文字列に変換した結果を取得
fun getStrFromTime(time: Long?): String {
    var timeStr = ""
    if (time != null) {
        timeStr = timeStr + (time / MINUTE_UNIT).toString() + ":"
        timeStr = timeStr + ((time % MINUTE_UNIT) / SEC_UNIT).toString() + ":"
        timeStr += ((time % SEC_UNIT) / MILL_SEC_UNIT).toString()
    }
    return timeStr
}


// HTMLの馬のリンク要素から文字列を取得
fun getHorseInfoFromLinkElement(linkElem: Element): Pair<String?, String?> {
    return Pair(
        linkElem.attr("href")?.split("/")?.last { it.isNotBlank() },
        linkElem.ownText().trim()
    )
}

// HTMLの騎手のリンク要素からID文字列を取得
fun getJockeyIdFromLinkElement(linkElem: Element): String? {
    return linkElem.attr("href")?.split("/")?.last { it.isNotBlank() }
}

// 分析の対象とする順位か
fun isTargetRank(rank: Int?): Boolean {
    if (rank == null) {
        return false
    } else {
        return rank in 1..3
    }
}
