package com.covit19.util

import com.covit19.model.api_view.CaseResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object CaseCaluculationUtil {
  // CaseResponseを日毎の差分をとって返す（昇順前提）
  fun getDiffCaseResponseByDay(caseCumulativeList: List<CaseResponse>): List<CaseResponse> {
    var diffList = mutableListOf<CaseResponse>()
    caseCumulativeList.withIndex().forEach {
      if (it.index != 0) {
        val c = it.value
        val beforeCase = caseCumulativeList[it.index - 1]
        diffList.add(
          CaseResponse(
            c.dailyDate,
            null,
            c.cases - beforeCase.cases,
            c.pcr - beforeCase.pcr,
            c.deaths - beforeCase.deaths,
            c.discharge - beforeCase.discharge
          )
        )
      }
    }
    return diffList;
  }
}
