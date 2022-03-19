package com.racingapi.horse.model.domain

import com.racingapi.horse.helper.getLocationFromElement
import com.racingapi.horse.helper.getTimeFromElement
import com.racingapi.horse.helper.isTargetRank
import org.jsoup.select.Elements

data class RaceResultByHorse(
    val raceId: String?,
    val raceName: String?,
    val location: String?,
    val wakuNum: Int?,
    val rank: Int,
    val courseType: String?,
    val courseCondition: String?,
    val distance: Int?,
    val timeMilli: Long
) {
    companion object {
        // 馬情報からレース結果を取得
        fun createRaceResultFromHorseElem(elems: Elements): RaceResultByHorse? {
            // 着順
            val rank = elems[11].ownText().trim().toIntOrNull()
            if (rank == null) {
                return null
            } else {
                // レース情報
                val race = elems[4].select("a")?.first()
                var raceId: String? = null
                var raceName: String? = null
                race?.also { r ->
                    raceId = r.attr("href")?.split("/")?.last { it.isNotBlank() }
                    raceName = r.ownText().trim()
                }
                // 競馬場
                val location = elems[1].select("a")?.first()?.let {
                    getLocationFromElement(it)
                }
                // 枠番
                val waku = elems[7].ownText().trim().toIntOrNull()
                // コース情報
                val courseInfo = elems[14].ownText().trim()
                val courseType = courseInfo[0].toString()
                val distance = courseInfo.drop(1).toIntOrNull()
                // 馬場状態
                val courseCondition = elems[15].ownText().trim()
                // タイム
                val time = getTimeFromElement(elems[17])
                return RaceResultByHorse(
                    raceId, raceName, location, waku, rank, courseType, courseCondition, distance, time
                )
            }
        }
    }
}

