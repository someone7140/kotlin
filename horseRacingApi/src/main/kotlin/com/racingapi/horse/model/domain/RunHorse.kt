package com.racingapi.horse.model.domain

import com.racingapi.horse.constants.*
import com.racingapi.horse.helper.*
import org.jsoup.nodes.Element

data class RunHorse(
    val horseId: String,
    val name: String,
    val horseUrl: String,
    val wakuNum: Int?,
    val jockeyId: String?,
    val gender: String?,
    val age: Int?,
    val detailInfo: HorseDetail?
) {

    companion object {
        fun createRunHorseFromElem(elem: Element?): RunHorse? {
            var runHorse: RunHorse? = null
            elem?.also { e ->
                // まずは馬のidと名前とurl
                val horseNameATag = e.select(HORSE_NAME_SELECTOR)?.select("a")
                horseNameATag?.first()?.also { aTag ->
                    val url = aTag.attr("href")
                    val (horseId, name) = getHorseInfoFromLinkElement(aTag)
                    if (url.isNotBlank() && !name.isNullOrEmpty() && horseId?.isNotBlank() == true) {
                        // 枠の取得
                        val waku = e.select(HORSE_WAKU_SELECTOR)?.first()?.children()?.firstNotNullOfOrNull { c ->
                            c.text().trim().toIntOrNull()
                        }
                        // 騎手の取得
                        val jockeyId = e.select(JOCKEY_SELECTOR)?.select("a")?.firstNotNullOfOrNull { a ->
                            getJockeyIdFromLinkElement(a)
                        }
                        // 性別・年齢
                        val (age, gender) = e.select(AGE_AND_GENDER_SELECTOR).first()?.let { ageAndGenderElem ->
                            getAgeAndGenderFromElement(ageAndGenderElem)
                        } ?: Pair(null, null)
                        // 馬の詳細情報（少し時間を置く）
                        Thread.sleep(500L)
                        val detailInfo = HorseDetail.createHorseDetailByHorseId(horseId)
                        runHorse = RunHorse(
                            horseId, name, url, waku, jockeyId, gender, age, detailInfo
                        )
                    }
                }
            }
            return runHorse
        }
    }

    // レースと比較対象の馬を指定してスコアを計算
    fun calcScore(
        raceName: String,
        courseType: String?,
        distance: Int?,
        location: String?,
        compareHorse: RaceResultByRace
    ): Long {
        var score = 0L
        // 相手馬の順位による係数
        val compareHorseRank = compareHorse.rank
        val factor = if (compareHorseRank == 1) {
            TOP_FACTOR
        } else if (compareHorseRank == 2) {
            SECOND_FACTOR
        } else if (compareHorseRank == 3) {
            THIRD_FACTOR
        } else {
            0
        }

        // 相手馬が自分か
        if (horseId == compareHorse.horseId) {
            return factor * SAME_HORSE_SCORE
        }

        // 当該レース類似度での結果のスコア
        score += getTargetRaceScore(courseType, distance, location, compareHorse)
        // 馬に関する属性の類似度による結果のスコア
        score += getTargetHorseAttrScore(compareHorse)
        // 当該レース以外での過去レースでの類似度による結果のスコア
        if (compareHorse.detail != null) {
            score += getPastRaceScore(raceName, compareHorse.detail.raceResults)
        }
        return score * factor
    }

    // 当該レース類似度での結果のスコア
    private fun getTargetRaceScore(
        courseType: String?,
        distance: Int?,
        location: String?,
        compareHorse: RaceResultByRace
    ): Long {
        var score = 0L
        // 枠が同じ
        if (wakuNum != null && wakuNum == compareHorse.wakuNum) {
            score += SAME_WAKU_SCORE
        }
        // 騎手が同じ
        if (jockeyId != null && jockeyId == compareHorse.jockeyId) {
            score += SAME_JOCKEY_SCORE
        }
        // 自分のレース結果と比較
        extractTargetCourseRace(courseType, distance, location).forEach {
            if (isTargetRank(it.rank)) {
                // 順位に応じてスコアを加算
                if (it.rank == 1) {
                    score += TARGET_RACE_TOP_SCORE
                }
                if (it.rank == 2) {
                    score += TARGET_RACE_SECOND_SCORE
                }
                if (it.rank == 3) {
                    score += TARGET_RACE_THIRD_SCORE
                }
                // コースコンディション
                if (it.courseCondition != null && it.courseCondition == compareHorse.courseCondition) {
                    score += TARGET_RACE_SAME_COURSE_CONDITION_SCORE
                    // タイムの比較
                    if (it.timeMilli < compareHorse.timeMilli) {
                        score += TARGET_RACE_TIME_SCORE
                    }
                }
            }
        }
        return score
    }

    // 馬に関する属性の類似度による結果のスコア
    private fun getTargetHorseAttrScore(compareHorse: RaceResultByRace): Long {
        var score = 0L
        // 性別が同じ
        if (gender != null && gender == compareHorse.gender) {
            score += SAME_GENDER_SCORE
        }
        // 年齢が同じ
        if (age != null && age == compareHorse.age) {
            score += SAME_AGE_SCORE
        }
        // 血統があるか
        detailInfo?.ancestorIds?.forEach { ancestorId ->
            if (compareHorse?.detail?.ancestorIds?.contains(ancestorId) == true) {
                score += SAME_ANCESTOR_SCORE
            }
        }
        return score
    }

    // 過去レース類似度での結果のスコア
    private fun getPastRaceScore(
        targetRaceName: String,
        compareHorseResults: List<RaceResultByHorse>
    ): Long {
        var score = 0L
        // 自分のレース結果と比較
        detailInfo?.raceResults?.forEach { ownResult ->
            if (isTargetRank(ownResult.rank)) {
                val compareResults = compareHorseResults.filter {
                    it.raceName?.contains(targetRaceName) != false &&
                            isTargetRank(it.rank) &&
                            ownResult.location != null && ownResult.location == it.location &&
                            ownResult.distance != null && ownResult.distance == it.distance &&
                            ownResult.courseType != null && ownResult.courseType == it.courseType
                }
                compareResults.forEach {
                    var tempScore = 0L
                    // 順位に応じてスコアを加算
                    if (it.rank == 1) {
                        tempScore += PAST_TARGET_RACE_TOP_SCORE
                    }
                    if (it.rank == 2) {
                        tempScore += PAST_TARGET_RACE_SECOND_SCORE
                    }
                    if (it.rank == 3) {
                        tempScore += PAST_TARGET_RACE_THIRD_SCORE
                    }
                    // コースコンディション
                    if (it.courseCondition != null && it.courseCondition == ownResult.courseCondition) {
                        tempScore += PAST_RACE_SAME_COURSE_CONDITION_SCORE
                        // タイムの比較
                        if (it.timeMilli < ownResult.timeMilli) {
                            tempScore += PAST_RACE_TIME_SCORE
                        }
                    }
                    // 自分の順位による係数
                    if (ownResult.rank == 1) {
                        tempScore *= PAST_OWN_RACE_TOP_FACTOR
                    } else if (ownResult.rank == 2) {
                        tempScore *= PAST_OWN_RACE_SECOND_FACTOR
                    } else if (ownResult.rank == 3) {
                        tempScore *= PAST_OWN_RACE_THIRD_FACTOR
                    } else {
                        tempScore *= 0
                    }
                    score += tempScore
                }
            }
        }
        return score
    }

    // 同コースでの最速タイム
    fun getSameCourseFastTimeMilli(
        courseType: String?,
        distance: Int?,
        location: String?
    ): String {
        val targetCourseRaces =
            extractTargetCourseRace(courseType, distance, location).filter { it.timeMilli > 0 }.map { it.timeMilli }
        return getStrFromTime(targetCourseRaces.minOrNull())
    }

    // 対象のレースと同じコースで抽出
    private fun extractTargetCourseRace(
        courseType: String?,
        distance: Int?,
        location: String?
    ): List<RaceResultByHorse> {
        if (courseType != null && distance != null && location != null && detailInfo?.raceResults != null) {
            return detailInfo.raceResults.filter {
                it.courseType == courseType && it.distance == distance && it.location == location
            }
        }
        return emptyList()
    }

}
