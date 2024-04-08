package com.lgcns.crossdev.onboarding1.data.mapper

import com.lgcns.crossdev.onboarding1.data.dto.CurrencyResponse
import com.lgcns.crossdev.onboarding1.data.entity.TravelEntity
import com.lgcns.crossdev.onboarding1.domain.model.Currency
import com.lgcns.crossdev.onboarding1.domain.model.Travel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Travel.toEntity(): TravelEntity {
    return TravelEntity(
        this.id,
        this.title,
        this.dateStart,
        this.dateEnd,
        this.img,
        this.currencyCodes
    )
}

fun TravelEntity.toModel() : Travel {
    return Travel(
        this.id,
        this.title,
        this.dateStart,
        this.dateEnd,
        this.img,
        this.currencyCodes
    )
}
