package com.covit19.batch

import com.covit19.command.Covid19JapanApiCommand
import com.covit19.service.Covid19SummaryService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class Covid19JapanApiBatch(
  private val restTemplate: RestTemplate,
  private val covid19SummaryService: Covid19SummaryService
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
    }
  }

}
