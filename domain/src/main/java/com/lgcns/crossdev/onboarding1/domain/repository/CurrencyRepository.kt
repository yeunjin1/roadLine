package com.lgcns.crossdev.onboarding1.domain.repository

import com.lgcns.crossdev.onboarding1.domain.common.LoadStatus
import com.lgcns.crossdev.onboarding1.domain.model.Currency
import com.lgcns.crossdev.onboarding1.domain.model.DomainResult
import com.lgcns.crossdev.onboarding1.domain.model.Travel
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun loadRemoteCurrency(): DomainResult<List<Currency>>
    suspend fun getAllCurrency(): Flow<List<Currency>>
}