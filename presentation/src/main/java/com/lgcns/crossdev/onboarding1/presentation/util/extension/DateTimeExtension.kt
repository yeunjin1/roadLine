package com.lgcns.crossdev.onboarding1.presentation.util.extension


fun hourMinToTotalMin(hour: Int, min: Int): Int {
    // 시간, 분 -> 분
    return hour * 60 + min
}

fun totalMinToHour(totalMin: Int): Int {
    return totalMin / 60
}

fun totalMinToMin(totalMin: Int): Int {
    return totalMin % 60
}

fun totalMinToString(totalMin: Int): String {
    var hour = totalMinToHour(totalMin)
    var min = totalMinToMin(totalMin).toString()
    if (min.length == 1) min = "0$min"
    return if(hour > 12) {
        hour -= 12
        "오후 $hour : $min"
    }
    else {
        "오전 $hour : $min"
    }
}

fun totalMinToStringShort(totalMin: Int?): String {
    totalMin?.let {
        val hour = totalMinToHour(totalMin)
        var min = totalMinToMin(totalMin).toString()
        if (min.length == 1) min = "0$min"
        return "$hour : $min"
    }
    return ""
}