package com.lgcns.crossdev.onboarding1.data.mapper

import com.lgcns.crossdev.onboarding1.data.dto.CurrencyResponse
import com.lgcns.crossdev.onboarding1.data.entity.CurrencyEntity
import com.lgcns.crossdev.onboarding1.data.entity.TravelEntity
import com.lgcns.crossdev.onboarding1.domain.model.Currency
import com.lgcns.crossdev.onboarding1.domain.model.Travel

object Mapper {
    fun mapperTravelDomain(entity: TravelEntity): Travel {
        return Travel(
            entity.id,
            entity.title,
            entity.dateStart,
            entity.dateEnd,
            entity.img
        )
    }
    fun mapperTravelEntity(domain: Travel): TravelEntity {
        return TravelEntity(
            domain.id,
            domain.title,
            domain.dateStart,
            domain.dateEnd,
            domain.img
        )
    }

    fun mapperCurrencyDomain(entity: CurrencyEntity): Currency {
        return Currency(
            entity.code,
            entity.name,
            entity.rate,
//            entity.symbol
        )
    }
    fun mapperCurrencyEntity(domain: Currency): CurrencyEntity {
        return CurrencyEntity(
            domain.code,
            domain.name,
            domain.rate,
//            domain.symbol
        )
    }

    fun mapperCurrencyEntity(response: CurrencyResponse): CurrencyEntity {
        return CurrencyEntity(
            response.code,
            response.name,
            response.rate.toDouble()
        )
    }


}