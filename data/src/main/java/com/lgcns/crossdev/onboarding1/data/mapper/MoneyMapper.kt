package com.lgcns.crossdev.onboarding1.data.mapper

import androidx.room.PrimaryKey
import com.lgcns.crossdev.onboarding1.data.entity.MoneyEntity
import com.lgcns.crossdev.onboarding1.data.entity.PlanEntity
import com.lgcns.crossdev.onboarding1.data.entity.TravelEntity
import com.lgcns.crossdev.onboarding1.domain.model.Money
import com.lgcns.crossdev.onboarding1.domain.model.Plan
import com.lgcns.crossdev.onboarding1.domain.model.Travel
import java.time.LocalDate

fun Money.toEntity(): MoneyEntity {
    return MoneyEntity(
        this.id,
        this.travelId,
        this.currencyCode,
        this.img,
        this.price!!,
        this.korPrice!!,
        this.category,
        this.date,
        this.memo,
    )
}

fun MoneyEntity.toModel(): Money {
    return Money(
        this.id,
        this.travelId,
        this.currencyCode,
        this.img,
        this.price,
        this.korPrice!!,
        this.category,
        this.date,
        this.memo,
    )
}
