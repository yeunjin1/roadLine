package com.lgcns.crossdev.onboarding1.data.datasource.remote

import com.lgcns.crossdev.onboarding1.data.dto.CurrencyResponse
import com.lgcns.crossdev.onboarding1.domain.common.exception.RoadLineException
import com.lgcns.crossdev.onboarding1.data.network.CurrencyApiClient
import com.lgcns.crossdev.onboarding1.data.util.Constants.CURRENCY_API_KEY
import com.lgcns.crossdev.onboarding1.data.util.Constants.CURRENCY_DATA_TYPE
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRemoteDataSource @Inject constructor(
    private val currencyApiClient: CurrencyApiClient
) {
    suspend fun loadCurrency(): Result<List<CurrencyResponse>> {
        return currencyApiClient.getCurrency(CURRENCY_API_KEY, CURRENCY_DATA_TYPE)
    }

    companion object {
        private const val TAG = "CurrencyRemoteDataSource"
    }
}