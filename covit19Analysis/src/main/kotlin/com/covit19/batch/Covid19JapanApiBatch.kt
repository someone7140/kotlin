package com.covit19.batch

import com.covit19.command.Covid19JapanApiCommand
import com.covit19.model.db.Covit19Summary
import com.covit19.service.Covid19DailyService
import com.covit19.service.Covid19SummaryService
import com.covit19.util.DateUtil
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate

@Component
class Covid19JapanApiBatch(
  private val restTemplate: RestTemplate,
  private val covid19SummaryService: Covid19SummaryService,
  private val covid19DailyService: Covid19DailyService
) {
  // initialDelay：アプリケーションが起動してから何秒後に実行するか（ミリ秒指定）
  // fixedDelay：何秒ごとに処理を実行するか（ミリ秒指定）
  // アプリケーション起動5秒後と4時間間隔で実行
  @Scheduled(initialDelay = 5L, fixedDelay = 14400000L)
  fun registDataFromCovid19JapanApi() {
    // APIからデータ取得
    val covit19SummaryList = Covid19JapanApiCommand.getPrefectureDataFromApi(restTemplate)
    // null判定
    covit19SummaryList?.let {
      val targetDate = it.first().key.dailyDate
      // サマリーの更新処理
      covid19SummaryService.updateSummaryData(targetDate, it)
      // 前日のサマリーデータを取得
      val yesterdaySummaryList = covid19SummaryService.getTargetDateSummaryDate(
        DateUtil.getMinusDate(targetDate, 1)
      )
      // デイリーの更新
      covid19DailyService.updateDailyData(targetDate, it, yesterdaySummaryList)
    }
  }

}
