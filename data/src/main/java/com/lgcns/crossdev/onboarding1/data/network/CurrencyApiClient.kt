package com.lgcns.crossdev.onboarding1.data.network

import com.lgcns.crossdev.onboarding1.data.dto.CurrencyResponse
import com.lgcns.crossdev.onboarding1.data.retrofit.ResultCallAdapterFactory
import com.lgcns.crossdev.onboarding1.data.util.Constants.CURRENCY_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiClient {
    @GET("exchangeJSON")
    suspend fun getCurrency(@Query("authkey") authKey: String
                            , @Query("data") dataType: String) : Result<List<CurrencyResponse>>

    companion object {
        fun create() : CurrencyApiClient {

            // 로그 메시지의 포맷을 설정.
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC  // 로그 레벨 BASIC
            }

            // OkHttpClient -> HTTP 통신의 결과를 로그 메시지로 출력해준다.
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(CURRENCY_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(ResultCallAdapterFactory())
                .build()
                .create(CurrencyApiClient::class.java)
        }
    }
}