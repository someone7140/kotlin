package com.covit19.model.api

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class Covit19PrefectureLastUpdated(
  val casesDate: Long,
  val deathsDate: Long,
  val pcrDate: Long,
  val hospitalizeDate: Long,
  val severeDate: Long,
  val dischargeDate: Long,
  val symptomConfirmingDate: Long
)
