package com.racingapi.horse.model.domain

import com.racingapi.horse.constants.*
import org.jsoup.nodes.Document

data class RaceInfo(
    val raceName: String,
    val courseType: String?,
    val distance: Int?,
    val location: String?,
    val runHorses: List<RunHorse>
) {

    companion object {
        fun createRaceInfoFromDoc(doc: Document): RaceInfo? {
            // レース名
            val raceNameElement = doc.select(RACE_NAME_SELECTOR)?.first()
            var raceInfo: RaceInfo? = null
            // レース名が取得できなかったら、そもそも指定したURLが違うと判断することにする
            raceNameElement?.also { elem ->
                // レース名
                val raceName = elem.ownText().trim()
                // コース情報
                var courseType: String? = null
                var distance: Int? = null
                val courseText = doc.select(RACE_COURSE_SELECTOR)?.select("span")?.first()?.ownText()?.trim()
                courseText?.also {
                    courseType = it[0].toString()
                    distance = it.drop(1).dropLast(1).toIntOrNull()
                }
                // 競馬場
                var location: String? = null
                val locationInfos = doc.select(RACE_LOCATION_SELECTOR)?.select("span")
                if (locationInfos?.isNotEmpty() == true && locationInfos.size > 1) {
                    location = locationInfos[1].ownText().trim()
                }
                // 指定したレースの馬のリスト
                val runHorses = doc
                    .select(RACE_TABLE_SELECTOR)
                    .select(HORSE_LIST_SELECTOR).mapNotNull {
                        RunHorse.createRunHorseFromElem(it)
                    }
                raceInfo = RaceInfo(raceName, courseType, distance, location, runHorses)
            }
            return raceInfo
        }
    }
}
