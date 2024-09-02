package com.lgcns.crossdev.onboarding1.test.model

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

data class Travel(
    val id: Long? = null,
    var title: String,
    var dateStart: LocalDate,
    var dateEnd: LocalDate,
    var img: String? = null,
    var currencyCodes: List<String> = emptyList()
): Serializable