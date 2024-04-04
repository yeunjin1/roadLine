package com.lgcns.crossdev.onboarding1.data.mapper

import com.lgcns.crossdev.onboarding1.data.dto.CurrencyResponse
import com.lgcns.crossdev.onboarding1.data.entity.CurrencyEntity
import com.lgcns.crossdev.onboarding1.domain.model.Currency
import java.text.NumberFormat
import java.util.Locale

private fun String.parseDouble(): Double {
    val format = NumberFormat.getInstance(Locale.getDefault())
    val number = format.parse(this)
    return number.toDouble()
}

fun List<CurrencyResponse>.toModel(): List<Currency> {
    return this.map {
        Currency(
            it.code,
            it.name,
            it.rate.parseDouble(),
        )
    }
}

fun List<CurrencyResponse>.toEntity(): List<CurrencyEntity> {
    return this.map {
        CurrencyEntity(
            it.code,
            it.name,
            it.rate.parseDouble(),
        )
    }
}