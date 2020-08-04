package com.covit19.model.api_view;

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class CaseResponse(
  val dailyDate: Long,
  val population: Long?,
  val cases: Long,
  val pcr: Long,
  val deaths: Long,
  val discharge: Long
)
