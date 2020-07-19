package com.covit19.model.db

import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "covit19_summary")
data class Covit19Summary(
  @EmbeddedId
  val key: Covit19Key,
  val population: Long,
  val cases: Long,
  val deaths: Long,
  val pcr: Long,
  val hospitalize: Long,
  val severe: Long,
  val discharge: Long,
  val symptomConfirming: Long
)
