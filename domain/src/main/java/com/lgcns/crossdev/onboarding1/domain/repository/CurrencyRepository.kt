package com.lgcns.crossdev.onboarding1.domain.repository

import com.lgcns.crossdev.onboarding1.domain.model.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface CurrencyRepository {
    suspend fun loadRemoteCurrency(): Result<List<Currency>>
    fun getAllCurrencies(): Flow<List<Currency>>
    suspend fun insertCurrencies(currencies: List<Currency>)
    fun getLatestCurrencyLoadDate(): String
    fun putLatestCurrencyLoadDate(date: String)
}