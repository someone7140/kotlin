package com.racingapi.horse.constants

// URL関連
const val NET_KEIBA_DOMAIN = "netkeiba.com"
const val NET_KEIBA_RACE_DOMAIN_WEB = "race.netkeiba.com"
const val NET_KEIBA_RACE_DOMAIN_SP = "race.sp.netkeiba.com"
const val NET_KEIBA_HORSE_DETAIL_URL = "https://db.netkeiba.com/horse/"
const val NET_KEIBA_RACE_DETAIL_URL = "https://db.netkeiba.com/race/"
const val NET_KEIBA_RACE_SEARCH_URL = "https://db.netkeiba.com/?pid=race_top"

// HTMLのセレクタ関連
const val RACE_NAME_SELECTOR = ".RaceName"
const val RACE_COURSE_SELECTOR = ".RaceData01"
const val RACE_LOCATION_SELECTOR = ".RaceData02"
const val RACE_TABLE_SELECTOR = "table[class*=\"RaceTable\"]"
const val HORSE_LIST_SELECTOR = ".HorseList"
const val HORSE_NAME_SELECTOR = ".Horse_Name , .HorseName"
const val HORSE_WAKU_SELECTOR = "td[class*=\"Waku\"]"
const val JOCKEY_SELECTOR = ".Jockey"
const val AGE_AND_GENDER_SELECTOR = ".Barei , .Lgt_Txt"
const val ODDS_SELECTOR = "span[id*=\"odds\"]"
const val BLOOD_TABLE_SELECTOR = ".blood_table"
const val ANCESTOR_SELECTOR = "td[class*=\"b_\"] > a"
const val PAST_RACE_INFO_SELECTOR = "diary_snap_cut > span"
const val HORSE_RACE_RESULT_TABLE_SELECTOR = "table.db_h_race_results"
const val PAST_RACE_RESULT_TABLE_SELECTOR = "table.race_table_01"
