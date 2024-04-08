package com.lgcns.crossdev.onboarding1.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class Travel(
    val id: Long? = null,
    var title: String,
    var dateStart: LocalDate,
    var dateEnd: LocalDate,
    val img: String? = null,
    val currencyCodes: List<String> = emptyList()
)