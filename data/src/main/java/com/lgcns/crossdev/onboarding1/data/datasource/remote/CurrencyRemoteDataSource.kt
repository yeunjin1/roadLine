package com.lgcns.crossdev.onboarding1.data.datasource.remote

import com.lgcns.crossdev.onboarding1.data.dto.ApiError
import com.lgcns.crossdev.onboarding1.data.dto.ApiResult
import com.lgcns.crossdev.onboarding1.data.dto.CurrencyResponse
import com.lgcns.crossdev.onboarding1.data.mapper.Mapper
import com.lgcns.crossdev.onboarding1.data.network.CurrencyApiClient
import com.lgcns.crossdev.onboarding1.data.util.Constants.CURRENCY_API_KEY
import com.lgcns.crossdev.onboarding1.data.util.Constants.CURRENCY_DATA_TYPE
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRemoteDataSource @Inject constructor(
    private val currencyApiClient: CurrencyApiClient
) {
//    override suspend fun loadCurrency(): Flow<List<CurrencyResponse>> = flow {
//        // 환율 정보 Map 조회
//        val currencyRateMap = mutableMapOf<String, Double>()
//        for(i in 1..5) {
//            Jsoup.connect(CURRENCY_RATE_CRAWLING_URL).get().select(".fxtable:nth-of-type(${i}) tr:nth-of-type(n+2)").forEach { element ->
//                val curName = element.select("td:nth-child(1)>a").text()
//                val curRate = element.select("td:nth-child(2)").text().toDouble()
//                currencyRateMap[curName] = curRate
//            }
//        }
//
//        // 전체 화폐 정보 조회
//        val currencyList = mutableListOf<CurrencyEntity>()
//        Jsoup.connect(CURRENCY_SYMBOL_CRAWLING_URL).get().run {
//            select(".fxtable tr:nth-of-type(n+2)").forEach { element ->
//                val curName = element.select("td:nth-child(1)>a").text()
//                val curCode = element.select("td:nth-child(2)").text()
//                val curSymbol = element.select("td:nth-child(3)").text().split(' ')[0].split(',')[0]
//                if (curCode.isNotEmpty()) {
//                    currencyList.add(CurrencyEntity(
//                        code = curCode,
//                        name = curName,
//                        rate = currencyRateMap[curName],
//                        symbol = curSymbol
//                    ))
//                }
//            }
//        }
//
//        emit(currencyList)


//    }


    suspend fun loadCurrency(): ApiResult<List<CurrencyResponse>> {
        return when(val result = currencyApiClient.getCurrency(CURRENCY_API_KEY, CURRENCY_DATA_TYPE)) {
            is ApiResult.Success -> {
                Timber.d("Success load currency ${result.value}", TAG)
                val response = result.value
                if (response.isSuccessful && response.body() != null && response.body()!!.isNotEmpty()) {
                    when(val result = response.body()!![0].result) {
                        1 -> ApiResult.Success(response.body())
                        else -> ApiResult.Failure(ApiError.CurrencyErrorResponse(code = result, message = ""))
                    }

                } else if (response.isSuccessful && response.body() == null) {
                    ApiResult.Failure(ApiError.EmptyBody())
                } else {
                    ApiResult.Failure(ApiError.EmptyBody())
                }
                ApiResult.Failure(ApiError.EmptyBody())
            }

            is ApiResult.Failure -> {
                Timber.d("Failed load currency ${result.cause}", TAG)
                ApiResult.Failure(result.cause)
            }
        }
    }

    companion object {
        private const val TAG = "CurrencyRemoteDataSource"
    }
}