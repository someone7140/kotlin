package com.covit19.repository

import com.covit19.model.db.Covit19Daily
import com.covit19.model.db.Covit19Summary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface Covit19DailyRepository : JpaRepository<Covit19Daily, Long> {
  @Query(value = "delete from covit19_daily where daily_date = :targetDate", nativeQuery = true)
  @Modifying
  fun deleteByDate(@Param("targetDate") targetDate: Long)

  @Query(value = "delete from covit19_daily where daily_date <= :deleteDate", nativeQuery = true)
  @Modifying
  fun deletePastRecord(@Param("deleteDate") targetDate: Long)

}

@Repository
interface Covit19SummaryRepository : JpaRepository<Covit19Summary, Long> {
  @Query(value = "delete from covit19_summary where daily_date = :targetDate", nativeQuery = true)
  @Modifying
  fun deleteByDate(@Param("targetDate") targetDate: Long)

  @Query(value = "delete from covit19_summary where daily_date <= :deleteDate", nativeQuery = true)
  @Modifying
  fun deletePastRecord(@Param("deleteDate") targetDate: Long)

  fun findByKeyDailyDate(targetDate: Long) : List<Covit19Summary>
}
