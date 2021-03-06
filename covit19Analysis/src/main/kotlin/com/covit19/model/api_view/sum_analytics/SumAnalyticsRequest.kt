package com.covit19.model.api_view.sum_analytics;

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class SumAnalyticsRequest(
  val startDate: Long,
  val endDate: Long,
  val intervalDate: Long
)
