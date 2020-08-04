package com.covit19.model.api_view;

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class TreatmentResponse(
  val dailyDate: Long,
  val hospitalize: Long,
  val severe: Long,
  val symptomConfirming: Long
)
