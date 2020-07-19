package com.covit19.util

import com.covit19.config.Covid19Constants
import com.covit19.model.api.Covit19Prefecture
import com.covit19.model.db.Covit19Summary
import com.covit19.service.Covid19DailyService
import com.covit19.service.Covid19SummaryService
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateUtil {

  val dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd")

  // 指定した日数分マイナスして日付の文字列を返す
  fun getMinusDate(targetDate: Long, minsuDay: Long): Long {
    return LocalDate.parse(targetDate.toString(), dateFormat)
      .minusDays(minsuDay).format(dateFormat).toLong()
  }

}
