package com.racingapi.horse.model.domain

import com.racingapi.horse.helper.getHorseInfoFromLinkElement
import com.racingapi.horse.helper.getAgeAndGenderFromElement
import com.racingapi.horse.helper.getJockeyIdFromLinkElement
import com.racingapi.horse.helper.getTimeFromElement
import org.jsoup.select.Elements

data class RaceResultByRace(
    val horseId: String,
    val name: String?,
    val rank: Int?,
    val wakuNum: Int?,
    val gender: String?,
    val jockeyId: String?,
    val age: Int?,
    val timeMilli: Long,
    val courseCondition: String?,
    val detail: HorseDetail?
) {
    companion object {
        // 過去レース情報からレース結果を取得
        fun createRaceResultFromRaceElem(elems: Elements, courseCondition: String?): RaceResultByRace? {
            var raceResult: RaceResultByRace? = null
            // 順位
            val rank = elems[0].ownText().trim().toIntOrNull()
            // 枠番号
            val wakuNum = elems[1].select("span")?.first()?.ownText()?.trim()?.toIntOrNull()
            // 馬名・ID
            val (horseId, horseName) = elems[3].select("a")?.first()?.let { horseElem ->
                getHorseInfoFromLinkElement(horseElem)
            } ?: Pair(null, null)
            // 年齢・性別
            var (age, gender) = elems[4]?.let { ageAndGenderElem ->
                getAgeAndGenderFromElement(ageAndGenderElem)
            } ?: Pair(null, null)
            // 騎手
            val jockeyId = elems[6].select("a")?.first()?.let { jockeyElem ->
                getJockeyIdFromLinkElement(jockeyElem)
            } ?: null
            // タイム
            val time = getTimeFromElement(elems[7])

            // 少し時間を置く
            Thread.sleep(500L)
            // 馬の詳細取得
            val detail = horseId?.let { id ->
                HorseDetail.createHorseDetailByHorseId(id)
            } ?: null

            if (!horseId.isNullOrBlank()) {
                raceResult = RaceResultByRace(
                    horseId, horseName, rank, wakuNum, gender, jockeyId, age, time, courseCondition, detail
                )
            }
            return raceResult
        }
    }
}
