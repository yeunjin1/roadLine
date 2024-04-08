package com.lgcns.crossdev.onboarding1.presentation.util.extension

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun periodToString(startDay: LocalDate, endDay: LocalDate): String {
    val dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd (E)")
    return startDay.format(dateFormat) + " - " + endDay.format(dateFormat)
}

fun LocalDate.getToday(format: String) = this.format(DateTimeFormatter.ofPattern(format))