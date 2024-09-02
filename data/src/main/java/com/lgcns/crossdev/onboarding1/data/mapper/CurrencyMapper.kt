package com.lgcns.crossdev.onboarding1.data.mapper

import com.lgcns.crossdev.onboarding1.data.dto.CurrencyResponse
import com.lgcns.crossdev.onboarding1.data.entity.CurrencyEntity
import com.lgcns.crossdev.onboarding1.domain.model.Currency
import com.lgcns.crossdev.onboarding1.mylibrary.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.NumberFormat
import java.time.LocalDateTime
import java.util.Locale

private fun String.parseDouble(): Double {
    val format = NumberFormat.getInstance(Locale.getDefault())
    val number = format.parse(this)
    return number.toDouble()
}

fun List<Currency>.toEntity(): List<CurrencyEntity> {
    return this.map {
        CurrencyEntity(
            it.code,
            it.name,
            it.rate,
        )
    }
}

fun Result<List<CurrencyResponse>>.toModel(): Result<List<Currency>> {
    this.onSuccess {
        return Result.success(it.map { res ->
            Currency(
                res.code,
                res.name,
                res.rate.parseDouble(),
            )
        })
    }.onFailure {
        return Result.failure(it)
    }
    return Result.failure(IllegalStateException("알 수 없는 오류가 발생했습니다."))
}

fun CurrencyEntity.toModel(): Currency {
    return Currency(
        this.code,
        this.name,
        this.rate,
    )
}

fun List<CurrencyEntity>.toModel(): List<Currency> {
    return this.map {
        Currency(
            it.code,
            it.name,
            it.rate,
        )
    }
}

fun Flow<List<CurrencyEntity>>.toModel(): Flow<List<Currency>> {
    return this.map { list ->
        list.map {
            Currency(
                it.code,
                it.name,
                it.rate,
            )
        }
    }
}