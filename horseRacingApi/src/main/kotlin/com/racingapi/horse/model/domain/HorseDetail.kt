package com.racingapi.horse.model.domain

import com.racingapi.horse.constants.*
import com.racingapi.horse.helper.getHtmlDoc

data class HorseDetail(
    val ancestorIds: Set<String>,
    val raceResults: List<RaceResultByHorse>
) {

    companion object {
        fun createHorseDetailByHorseId(horseId: String): HorseDetail? {
            var horseDetail: HorseDetail? = null
            // 馬の詳細情報
            val horseDetailDoc = getHtmlDoc(NET_KEIBA_HORSE_DETAIL_URL + horseId)
            horseDetailDoc?.also { doc ->
                // 祖先の取得
                val ancestorIds =
                    doc.select(BLOOD_TABLE_SELECTOR)?.first()?.select(ANCESTOR_SELECTOR)?.map { ancestor ->
                        ancestor.attr("href").split("/")?.last { it.isNotBlank() }
                    }?.toHashSet() ?: emptySet()
                // レース結果の取得（3着まで）
                val raceResults = doc.select(HORSE_RACE_RESULT_TABLE_SELECTOR)?.first()?.select("tbody > tr")?.mapNotNull { tr ->
                    val cols = tr.select("td")
                    if (cols?.isNotEmpty() == true && cols.size > 20) {
                        RaceResultByHorse.createRaceResultFromHorseElem(cols)
                    } else {
                        null
                    }
                } ?: emptyList<RaceResultByHorse>()

                horseDetail = HorseDetail(ancestorIds, raceResults)
            }
            return horseDetail
        }
    }
}
