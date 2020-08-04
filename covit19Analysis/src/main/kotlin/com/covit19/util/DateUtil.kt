package com.covit19.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtil {
  private const val maxIntervalDay = 185
  private val dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd")

  // 指定した日数分マイナスして日付をLongを返す
  fun getMinusDate(targetDate: Long, minsuDay: Long): Long {
    return LocalDate.parse(targetDate.toString(), dateFormat)
      .minusDays(minsuDay).format(dateFormat).toLong()
  }

  // 指定した期間の日付リストを取得
  fun getDateList(recentDateFromDb: Long, startDate: Long, endDate: Long, interval: Long): List<Long> {
    val dateList = mutableListOf<Long>()
    val recentDate = if (recentDateFromDb > endDate) endDate else recentDateFromDb
    var beforeDateCount = interval
    var targetDate = recentDate
    while (beforeDateCount <= maxIntervalDay && targetDate >= startDate) {
      dateList.add(targetDate)
      targetDate = getMinusDate(recentDate, beforeDateCount)
      beforeDateCount += interval
    }
    return dateList
  }

}
