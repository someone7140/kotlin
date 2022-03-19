package com.racingapi.horse.model.domain

data class RaceAnalyticsResult(
    val raceName: String,
    val horseId: String,
    val horseName: String,
    val horseUrl: String,
    val sameCourseFastTime: String,
    val score: Long
) {
    companion object {
        fun createRaceAnalyticsResults(
            horse: RunHorse,
            raceName: String,
            courseType: String?,
            distance: Int?,
            location: String?,
            pastResults: List<RaceResultByRace>
        ): RaceAnalyticsResult {
            // スコア
            var score = 0L
            pastResults.forEach {
                score += horse.calcScore(
                    raceName, courseType, distance, location, it
                )
            }
            // 同コースでの最速タイム
            val sameCourseFastTimeMilli = horse.getSameCourseFastTimeMilli(courseType, distance, location)
            return RaceAnalyticsResult(
                raceName, horse.horseId, horse.name, horse.horseUrl, sameCourseFastTimeMilli, score
            )
        }
    }
}
