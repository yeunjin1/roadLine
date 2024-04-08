package com.lgcns.crossdev.onboarding1.data.repository

import com.lgcns.crossdev.onboarding1.data.datasource.local.CurrencyLocalDataSource
import com.lgcns.crossdev.onboarding1.data.datasource.remote.CurrencyRemoteDataSource
import com.lgcns.crossdev.onboarding1.data.mapper.toEntity
import com.lgcns.crossdev.onboarding1.data.mapper.toModel
import com.lgcns.crossdev.onboarding1.domain.model.Currency
import com.lgcns.crossdev.onboarding1.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepositoryImpl @Inject constructor(
    private val currencyRemoteDataSource: CurrencyRemoteDataSource,
    private val currencyLocalDataSource: CurrencyLocalDataSource
): CurrencyRepository {
    override suspend fun loadRemoteCurrency(): Result<List<Currency>> = currencyRemoteDataSource.loadCurrency().toModel()

    override fun getAllCurrencies(): Flow<List<Currency>> = currencyLocalDataSource.getAllCurrency().toModel()

    override suspend fun insertCurrencies(currencies: List<Currency>) = currencyLocalDataSource.insertCurrencies(currencies.toEntity())

    override fun getLatestCurrencyLoadDate(): String = currencyLocalDataSource.getLatestCurrencyLoadDate()

    override fun putLatestCurrencyLoadDate(date: String) = currencyLocalDataSource.putLatestCurrencyLoadDate(date)
}