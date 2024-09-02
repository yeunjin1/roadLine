package com.lgcns.crossdev.onboarding1.data.datasource.local

import android.content.Context
import com.lgcns.crossdev.onboarding1.data.dao.CurrencyDao
import com.lgcns.crossdev.onboarding1.data.entity.CurrencyEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class CurrencyLocalDataSource @Inject constructor(
    private val currencyDao: CurrencyDao,
    @ApplicationContext private val context: Context
) {
    private val prefs by lazy {
        context.getSharedPreferences("${context.packageName}.preference", Context.MODE_PRIVATE)
    }

    fun getAllCurrency(): Flow<List<CurrencyEntity>> = currencyDao.getAllCurrency()
    fun getCurrenciesByCode(code: List<String>): Flow<List<CurrencyEntity>> = currencyDao.getCurrenciesByCode(code)
    suspend fun insertCurrencies(currencies: List<CurrencyEntity>) = currencyDao.insertCurrencies(currencies)
    fun getLatestCurrencyLoadDate(): String = prefs.getString("latestCurrencyLoadDate", "").toString()
    fun putLatestCurrencyLoadDate(date: String) = prefs.edit().putString("latestCurrencyLoadDate", date).apply()
    suspend fun deleteCurrencies() = currencyDao.deleteCurrencies()
}