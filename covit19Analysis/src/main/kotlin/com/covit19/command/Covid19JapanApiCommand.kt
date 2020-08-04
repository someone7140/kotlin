package com.covit19.command

import com.covit19.model.api.Covit19Prefecture
import com.covit19.model.db.Covit19Key
import com.covit19.model.db.Covit19Summary
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate

object Covid19JapanApiCommand {

  // 都道府県別データを取得する
  fun getPrefectureDataFromApi(restTemplate: RestTemplate): List<Covit19Summary>? {
    // APIのコール
    val result = restTemplate.exchange("https://covid19-japan-web-api.now.sh/api/v1/prefectures", HttpMethod.GET, null, Array<Covit19Prefecture>::class.java)
    // 更新日付の統一チェック
    val dateSet = result.body?.map {
      val lastUpdated = it.lastUpdated
      setOf(
              lastUpdated.casesDate,
              lastUpdated.deathsDate,
              lastUpdated.pcrDate,
              lastUpdated.hospitalizeDate,
              lastUpdated.severeDate,
              lastUpdated.dischargeDate,
              lastUpdated.symptomConfirmingDate)
    }?.flatten()?.toSet()
    // 更新日付が統一されている
    if(!dateSet.isNullOrEmpty() && dateSet.size == 1) {
      val targetDate = dateSet.first()
      return result.body?.map {
        Covit19Summary(
          Covit19Key(it.id, targetDate),
          it.population,
          it.cases,
          it.deaths,
          it.pcr,
          it.hospitalize,
          it.severe,
          it.discharge,
          it.symptomConfirmingDate
        )
      }
    } else {
      return null
    }
  }

}
