package com.racingapi.horse.useCase

import com.racingapi.horse.constants.*
import com.racingapi.horse.helper.getHtmlDoc
import com.racingapi.horse.helper.getHtmlDocWithForm
import com.racingapi.horse.helper.getLocationFromElement
import com.racingapi.horse.helper.isTargetRank
import com.racingapi.horse.model.domain.RaceAnalyticsResult
import com.racingapi.horse.model.domain.RaceInfo
import com.racingapi.horse.model.domain.RaceResultByRace
import org.jsoup.nodes.Element
import java.net.URLEncoder

// 指定したURLのレースの分析結果を取得
fun analyticsRace(url: String): List<RaceAnalyticsResult>? {
    val htmlDoc = getHtmlDoc(url.replace(NET_KEIBA_RACE_DOMAIN_SP, NET_KEIBA_RACE_DOMAIN_WEB))
    var results: List<RaceAnalyticsResult>? = emptyList()
    htmlDoc?.also { doc ->
        RaceInfo.createRaceInfoFromDoc(doc)?.also { r ->
            // 過去のレース結果情報
            val pastRaceResults = getPastRaceResults(r.raceName, r.location ?: "")
            // 分析結果
            results = r.runHorses.map { h ->
                RaceAnalyticsResult.createRaceAnalyticsResults(
                    h, r.raceName, r.courseType, r.distance, r.location, pastRaceResults
                )
            }.sortedByDescending {
                it.score
            }
        }
    }
    return results
}

// レース名を指定して過去レースを取得
fun getPastRaceResults(raceName: String, location: String): List<RaceResultByRace> {
    var raceResult: List<RaceResultByRace> = emptyList()
    // レース名で検索
    val formData = HashMap<String, String>()
    formData.put("pid", "race_list");
    formData.put("word", URLEncoder.encode(raceName, "EUC-JP"));
    val doc = getHtmlDocWithForm(NET_KEIBA_RACE_SEARCH_URL, formData)
    val pastResultRecords =
        doc?.select(PAST_RACE_RESULT_TABLE_SELECTOR)?.first()?.select("tr")?.drop(1) ?: emptyList<Element>()
    // 新着の3レースが対象
    var count = 0
    run loop@{
        pastResultRecords.forEach { tr ->
            val column = tr.select("td")
            if (column.size > 5) {
                val elemLocation = column[1].select("a")?.first()?.let {
                    getLocationFromElement(it)
                }
                // 競馬場の一致判定
                if (!elemLocation.isNullOrEmpty() && elemLocation == location) {
                    val raceId = column[4].select("a")?.attr("href")?.split("/")?.last {
                        it.isNotBlank()
                    }
                    raceId?.also { rid ->
                        // 少し時間を置く
                        Thread.sleep(500L)
                        val raceDetailDoc = getHtmlDoc(NET_KEIBA_RACE_DETAIL_URL + rid)
                        // コースコンディション
                        val condition = raceDetailDoc?.select(PAST_RACE_INFO_SELECTOR)?.first()?.let {
                            var resCondition: String? = null
                            val splitRaceInfos = it.ownText().split("/")
                            if (splitRaceInfos.size > 2) {
                                val splitCourseInfos = splitRaceInfos[2].split(":")
                                if (splitCourseInfos.size > 1) {
                                    resCondition = splitCourseInfos[1].trim().firstOrNull()?.let { it.toString() }
                                }
                            }
                            resCondition
                        }
                        // 着順情報
                        raceDetailDoc?.select(PAST_RACE_RESULT_TABLE_SELECTOR)?.first()?.select("tr")?.withIndex()
                            ?.forEach { raceRecord ->
                                // 3着以内
                                if (isTargetRank(raceRecord.index)) {
                                    val cols = raceRecord.value.select("td")
                                    if (cols.isNotEmpty() && cols.size > 15) {
                                        val result = RaceResultByRace.createRaceResultFromRaceElem(cols, condition)
                                        result?.also {
                                            raceResult = raceResult.plus(it)
                                            count++
                                        }
                                    }
                                }
                            }
                    }
                }
            }
            // 3件過去レース取得したらループを抜ける
            if (count >= 3) {
                return@loop
            }
        }
    }
    return raceResult
}