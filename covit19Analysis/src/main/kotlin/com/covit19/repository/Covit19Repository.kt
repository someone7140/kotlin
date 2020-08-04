package com.covit19.repository

import com.covit19.model.db.Covit19Summary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface Covit19SummaryRepository : JpaRepository<Covit19Summary, Long> {
  @Query(value = "delete from covit19_summary where daily_date = :targetDate", nativeQuery = true)
  @Modifying
  fun deleteByDate(@Param("targetDate") targetDate: Long)

  @Query(value = "delete from covit19_summary where daily_date <= :deleteDate", nativeQuery = true)
  @Modifying
  fun deletePastRecord(@Param("deleteDate") targetDate: Long)

  fun findByKeyDailyDate(targetDate: Long) : List<Covit19Summary>

  @Query(value = "select max(daily_date) from covit19_summary", nativeQuery = true)
  fun selectRecentDate() :Long

  @Query(value = "select daily_date, sum(population) as population, sum(cases) as cases, sum(pcr) as pcr, sum(deaths) as deaths, sum(discharge) as discharge,"
          + "sum(hospitalize) as hospitalize, sum(severe) as severe, sum(symptom_confirming) as symptom_confirming "
          + "from covit19_summary where daily_date IN :dateList group by daily_date order by daily_date", nativeQuery = true)
  fun selectCaseSumData(@Param("dateList") dateList: List<Long>) : List<Any>

  @Query(value = "select * from covit19_summary where daily_date IN :dateList and prefecture_id IN :prefectureCodeList order by daily_date, prefecture_id", nativeQuery = true)
  fun selectCasePrefectureData(@Param("prefectureCodeList") prefectureCodeList: List<Int>, @Param("dateList") dateList: List<Long>) : List<Covit19Summary>
}
