package com.covit19.service

import com.covit19.model.db.Covit19Summary
import com.covit19.repository.Covit19SummaryRepository
import com.covit19.util.DateUtil
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class Covid19SummaryService(private val repository: Covit19SummaryRepository) {
  // サマリーのupdate
  fun updateSummaryData(
    targetDate: Long,
    covit19SummaryList: List<Covit19Summary>
  ) {
    // 101日以前のレコードを削除
    val deleteDate = DateUtil.getMinusDate(targetDate, 101)
    repository.deletePastRecord(deleteDate)
    // 直近日付のdelete・insert
    repository.deleteByDate(targetDate)
    repository.saveAll(covit19SummaryList)
  }

  // 日付指定してサマリーデータを取得
  fun getTargetDateSummaryDate(targetDate: Long) : List<Covit19Summary> {
    return repository.findByKeyDailyDate(targetDate)
  }

}
