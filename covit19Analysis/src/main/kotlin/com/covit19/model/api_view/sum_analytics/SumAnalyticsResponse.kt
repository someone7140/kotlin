package com.covit19.model.api_view.sum_analytics;

import com.covit19.model.api_view.CaseResponse
import com.covit19.model.api_view.TreatmentResponse
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class SumAnalyticsResponse(
  val caseCumulativeList: List<CaseResponse>,
  val caseTransitionList: List<CaseResponse>,
  val treatmentList: List<TreatmentResponse>
)
