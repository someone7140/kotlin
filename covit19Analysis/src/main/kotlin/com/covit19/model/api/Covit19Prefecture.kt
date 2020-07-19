package com.covit19.model.api

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class Covit19Prefecture(
  val id: Int,
  val population: Long,
  val lastUpdated: Covit19PrefectureLastUpdated,
  val cases: Long,
  val deaths: Long,
  val pcr: Long,
  val hospitalize: Long,
  val severe: Long,
  val discharge: Long,
  val symptomConfirmingDate: Long
)
