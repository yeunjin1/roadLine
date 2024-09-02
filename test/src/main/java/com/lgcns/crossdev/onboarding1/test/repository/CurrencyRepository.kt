package com.lgcns.crossdev.onboarding1.test.repository

import com.lgcns.crossdev.onboarding1.test.model.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun loadRemoteCurrency(): Result<List<Currency>>
    fun getAllCurrencies(): Flow<List<Currency>>
    fun getCurrenciesByCode(code: List<String>): Flow<List<Currency>>
    suspend fun insertCurrencies(currencies: List<Currency>)
    fun getLatestCurrencyLoadDate(): String
    fun putLatestCurrencyLoadDate(date: String)
    suspend fun deleteCurrencies()

}