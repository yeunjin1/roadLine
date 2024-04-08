package com.lgcns.crossdev.onboarding1.domain.model

import java.time.LocalDateTime

data class Currency(
    val code: String,
    val name: String,
    val rate: Double?,
)