package com.covit19.service

import com.covit19.model.db.Covit19Daily
import com.covit19.model.db.Covit19Summary
import com.covit19.repository.Covit19DailyRepository
import com.covit19.util.DateUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class Covid19DailyService(private val repository: Covit19DailyRepository) {

  // デイリーのデータupdate
  fun updateDailyData(
    targetDate: Long,
    todaySummaryList: List<Covit19Summary>,
    yesterdaySummaryList: List<Covit19Summary>) {
    // 昨日のデータが取れているときのみ実行
    if (yesterdaySummaryList.isNotEmpty()) {
      // 100日以前を削除
      val deleteDate = DateUtil.getMinusDate(targetDate, 101)
      repository.deletePastRecord(deleteDate)
      // 直近日付のdelete・insert
      repository.deleteByDate(targetDate)
      // 直近日付と前日の差分をinsetデータとする
      val insertList = todaySummaryList.map { today ->
        yesterdaySummaryList.find { yesterday ->
          yesterday.key.prefectureId == today.key.prefectureId
        }?.let {
            Covit19Daily(
              today.key,
              today.cases - it.cases,
              today.deaths - it.deaths,
              today.pcr - it.pcr,
              today.hospitalize - it.hospitalize,
              today.severe - it.severe,
              today.discharge - it.discharge,
              today.symptomConfirming - it.symptomConfirming
            )
        } ?: run {
          Covit19Daily(
            today.key,
            today.cases,
            today.deaths,
            today.pcr,
            today.hospitalize,
            today.severe,
            today.discharge,
            today.symptomConfirming
          )
        }
      }
      repository.saveAll(insertList)
    }
  }
}
