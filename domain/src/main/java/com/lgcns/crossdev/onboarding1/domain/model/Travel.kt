package com.lgcns.crossdev.onboarding1.domain.model

import java.time.LocalDate

data class Travel(
    val id: Long? = null,
    var title: String,
    var dateStart: LocalDate,
    var dateEnd: LocalDate,
    val img: String? = null
)