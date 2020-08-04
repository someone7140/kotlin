package com.covit19.service

import com.covit19.model.db.Covit19Summary
import com.covit19.repository.Covit19SummaryRepository
import com.covit19.util.DateUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class Covid19SummaryService(
  private val repository: Covit19SummaryRepository
) {
  // サマリーのupdate
  fun updateSummaryData(
    targetDate: Long,
    covit19SummaryList: List<Covit19Summary>
  ) {
    // 186日以前のレコードを削除
    val deleteDate = DateUtil.getMinusDate(targetDate, 186)
    repository.deletePastRecord(deleteDate)
    // 直近日付のdelete・insert
    repository.deleteByDate(targetDate)
    repository.saveAll(covit19SummaryList)
  }

  // 日付指定してサマリーデータを取得
  fun getTargetDateSummaryDate(targetDate: Long) : List<Covit19Summary> {
    return repository.findByKeyDailyDate(targetDate)
  }

  // テーブルの内の最も最近の日付を取得
  fun getRecentDate() : Long {
    return repository.selectRecentDate()
  }

  // 日付を指定してサマリーの集計データを取得
  fun getSummaryAnalyticsDate(dateList: List<Long>) : List<Any> {
    return repository.selectCaseSumData(dateList)
  }

  // 日付を指定して都道府県別データを取得
  fun getPrefectureAnalyticsDate(prefectureCodeList: List<Int>, dateList: List<Long>) : List<Covit19Summary> {
    return repository.selectCasePrefectureData(prefectureCodeList, dateList)
  }

}
