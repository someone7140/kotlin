package com.covit19.controller

import com.covit19.model.api_view.CaseResponse
import com.covit19.model.api_view.TreatmentResponse
import com.covit19.model.api_view.sum_analytics.SumAnalyticsRequest
import com.covit19.model.api_view.sum_analytics.SumAnalyticsResponse
import com.covit19.service.Covid19SummaryService
import com.covit19.util.CaseCaluculationUtil
import com.covit19.util.DateUtil
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sum_analitycs")
class SumAnalyticsController(
  private val covid19SummaryService: Covid19SummaryService
) {

  // サマリーのデータを取得
  @RequestMapping(method = [RequestMethod.POST])
  @CrossOrigin
  fun getSumData(@RequestBody req: SumAnalyticsRequest): SumAnalyticsResponse {
    // サマリーのテーブルから一番最後の日を取得
    val recentDate = covid19SummaryService.getRecentDate()
    // 対象とする日付のリストを取得
    val targetDateList = DateUtil.getDateList(recentDate, req.startDate, req.endDate, req.intervalDate)

    // サマリーデータからの取得
    val summaryAnalyticsList = covid19SummaryService.getSummaryAnalyticsDate(targetDateList)
    val caseCumulativeList = mutableListOf<CaseResponse>()
    val treatmentList = mutableListOf<TreatmentResponse>()
    summaryAnalyticsList.forEach {
      val columns = it as? Array<Any>
      columns?.let { c ->
        caseCumulativeList.add(
          CaseResponse(
            c[0].toString().toLong(),
            c[1].toString().toLong(),
            c[2].toString().toLong(),
            c[3].toString().toLong(),
            c[4].toString().toLong(),
            c[5].toString().toLong()
          )
        )
        treatmentList.add(
          TreatmentResponse(
            c[0].toString().toLong(),
            c[6].toString().toLong(),
            c[7].toString().toLong(),
            c[8].toString().toLong()
          )
        )
      }
    }

    return SumAnalyticsResponse(
      caseCumulativeList,
      CaseCaluculationUtil.getDiffCaseResponseByDay(caseCumulativeList),
      treatmentList
    )
  }
}
