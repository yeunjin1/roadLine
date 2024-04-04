package com.lgcns.crossdev.onboarding1.data.repository

import com.lgcns.crossdev.onboarding1.data.datasource.local.CurrencyLocalDataSource
import com.lgcns.crossdev.onboarding1.data.datasource.remote.CurrencyRemoteDataSource
import com.lgcns.crossdev.onboarding1.data.dto.ApiResult
import com.lgcns.crossdev.onboarding1.data.entity.CurrencyEntity
import com.lgcns.crossdev.onboarding1.data.mapper.Mapper
import com.lgcns.crossdev.onboarding1.data.mapper.toEntity
import com.lgcns.crossdev.onboarding1.data.mapper.toModel
import com.lgcns.crossdev.onboarding1.domain.common.LoadStatus
import com.lgcns.crossdev.onboarding1.domain.model.Currency
import com.lgcns.crossdev.onboarding1.domain.model.DomainResult
import com.lgcns.crossdev.onboarding1.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepositoryImpl @Inject constructor(
    private val currencyRemoteDataSource: CurrencyRemoteDataSource,
    private val currencyLocalDataSource: CurrencyLocalDataSource
): CurrencyRepository {
    override suspend fun loadRemoteCurrency(): DomainResult<List<Currency>> {
        when(val result = currencyRemoteDataSource.loadCurrency()) {
            is ApiResult.Success -> {
                currencyLocalDataSource.insertCurrencies(result.value.toEntity())
            }
            is ApiResult.Failure -> {
            }
        }
    }

    override suspend fun getAllCurrency(): Flow<List<Currency>> {
        return currencyLocalDataSource.getAllCurrency()
            .map { entityList -> entityList.map { entity -> Mapper.mapperCurrencyDomain(entity) } }
    }

//    override suspend fun loadCurrency(): Flow<List<Currency>> {
//        var currencyList = emptyList<CurrencyEntity>()
//        var currencyDomainList = emptyList<Currency>()
//        currencyLocalDataSource.loadCurrency()
//            .onCompletion { currencyDomainList = currencyList.map { Mapper.mapperCurrencyDomain(it) } }
//            .collect { localCurrencyList ->
//                if(localCurrencyList.isEmpty()) {
//                    currencyRemoteDataSource.loadCurrency().collect { remoteCurrencyList ->
//                        currencyList = remoteCurrencyList
//                        Timber.d("remoteCurrencyList : $remoteCurrencyList")
//                    }
//                }
//                else {
//                    currencyList = localCurrencyList
//                    Timber.d("localCurrencyList : $localCurrencyList")
//                }
//            }
//        return flowOf(currencyDomainList)
//    }

}