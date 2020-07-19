package com.covit19.model.db

import java.io.Serializable
import javax.persistence.Embeddable

@Embeddable
data class Covit19Key (
  val prefectureId: Int = 0,
  val dailyDate: Long = 19900101
) : Serializable
