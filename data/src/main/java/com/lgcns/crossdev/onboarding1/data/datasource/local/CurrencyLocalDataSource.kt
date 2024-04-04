package com.lgcns.crossdev.onboarding1.data.datasource.local

import com.lgcns.crossdev.onboarding1.data.dao.CurrencyDao
import com.lgcns.crossdev.onboarding1.data.entity.CurrencyEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CurrencyLocalDataSource @Inject constructor(
    private val currencyDao: CurrencyDao
) {
    fun getAllCurrency(): Flow<List<CurrencyEntity>> = currencyDao.getAllCurrency()
    suspend fun insertCurrencies(currencies: List<CurrencyEntity>) = currencyDao.insertCurrencies(currencies)
}