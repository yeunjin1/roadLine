package com.lgcns.crossdev.onboarding1.presentation.util.extension

import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

fun periodToString(startDay: LocalDate, endDay: LocalDate): String {
    val dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd (E)")
    return startDay.format(dateFormat) + " - " + endDay.format(dateFormat)
}

fun LocalDate.toStringDate(pattern: String = "yyyy.MM.dd (E)"): String {
    val dateFormat = DateTimeFormatter.ofPattern(pattern)
    return this.format(dateFormat)
}

fun LocalDate.getToday(format: String) = this.format(DateTimeFormatter.ofPattern(format))

fun Double.toFormatPrice(code: String): String {
    return if(code == "KRW") {
        DecimalFormat("###,###").format(this.roundToInt())
    }
    else {
        DecimalFormat("###,###.##").format(this)
    }
}