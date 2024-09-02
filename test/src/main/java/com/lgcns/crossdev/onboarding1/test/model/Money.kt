package com.lgcns.crossdev.onboarding1.test.model

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

data class Money(
    val id: Long? = null,
    val travelId: Long,
    val currencyCode: String = "KRW",
    var img: String? = null,
    var price: Double? = null,
    var korPrice: Double? = null,
    var category: Int = 0,
    var date: LocalDate,
    var memo: String? = null,
): Serializable


