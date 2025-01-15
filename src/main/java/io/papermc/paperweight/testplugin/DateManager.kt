package io.papermc.paperweight.testplugin

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

object DateManager {
  private val dateFormat: String = "yyyy-MM-dd HH:mm:ss"
  private val formatter = SimpleDateFormat(dateFormat)

  // 현재 날짜를 문자열로 반환
  fun getCurrentDateAsString(): String {
    val currentDate = Date()
    return formatter.format(currentDate)
  }

  // Date 객체를 문자열로 변환
  fun formatDateToString(date: Date): String {
    return formatter.format(date)
  }

  // 문자열을 Date 객체로 변환
  fun parseStringToDate(dateString: String): Date? {
    return try {
      formatter.parse(dateString)
    } catch (e: Exception) {
      println("Invalid date string: $dateString")
      null
    }
  }
  fun dateToInt(date: Date) : Long {
    val epochDate = LocalDate.of(1970, 1, 1)
    // 현재 날짜 가져오기
    // 두 날짜 사이의 일수 계산
    val daysBetween = ChronoUnit.DAYS.between(epochDate, date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
    return daysBetween
  }
}
