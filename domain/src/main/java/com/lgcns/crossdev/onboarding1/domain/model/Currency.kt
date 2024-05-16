package com.lgcns.crossdev.onboarding1.domain.model

import java.time.LocalDateTime

data class Currency(
    var code: String,
    val name: String,
    var rate: Double?,
)