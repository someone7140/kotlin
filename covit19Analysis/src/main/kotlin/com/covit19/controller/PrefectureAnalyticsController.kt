package com.covit19.controller

import com.covit19.model.api_view.CaseResponse
import com.covit19.model.api_view.TreatmentResponse
import com.covit19.model.api_view.prefecture_analytics.PrefectureAnalitycsRequest
import com.covit19.model.api_view.prefecture_analytics.PrefectureAnalitycsResponse
import com.covit19.model.api_view.sum_analytics.SumAnalyticsRequest
import com.covit19.model.api_view.sum_analytics.SumAnalyticsResponse
import com.covit19.service.Covid19SummaryService
import com.covit19.util.CaseCaluculationUtil
import com.covit19.util.DateUtil
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/prefecture_analitycs")
class PrefectureAnalyticsController(
  private val covid19SummaryService: Covid19SummaryService
) {

  // 都道府県別のデータを取得
  @RequestMapping(method = [RequestMethod.POST])
  @CrossOrigin
  fun getPrefectureData(@RequestBody req: PrefectureAnalitycsRequest): List<PrefectureAnalitycsResponse> {
    // サマリーのテーブルから一番最後の日を取得
    val recentDate = covid19SummaryService.getRecentDate()
    // 対象とする日付のリストを取得
    val targetDateList = DateUtil.getDateList(recentDate, req.startDate, req.endDate, req.intervalDate)

    // サマリーデータからの取得
    val prefectureAnalyticsList = covid19SummaryService.getPrefectureAnalyticsDate(req.prefectureCodeList, targetDateList)

    return prefectureAnalyticsList.groupBy { it.key.prefectureId }.map {
      val analyticsList = it.value
      val caseCumulativeList = mutableListOf<CaseResponse>()
      val treatmentList = mutableListOf<TreatmentResponse>()
      analyticsList.forEach { a ->
        caseCumulativeList.add(
          CaseResponse(
            a.key.dailyDate,
            a.population,
            a.cases,
            a.pcr,
            a.deaths,
            a.discharge
          )
        )
        treatmentList.add(
          TreatmentResponse(
            a.key.dailyDate,
            a.hospitalize,
            a.severe,
            a.symptomConfirming
          )
        )
      }
      PrefectureAnalitycsResponse(
        it.key,
        caseCumulativeList,
        CaseCaluculationUtil.getDiffCaseResponseByDay(caseCumulativeList),
        treatmentList
      )
    }
  }
}
