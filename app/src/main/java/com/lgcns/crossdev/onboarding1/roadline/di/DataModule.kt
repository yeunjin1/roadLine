package com.lgcns.crossdev.onboarding1.roadline.di

import android.content.Context
import androidx.room.Room
import com.lgcns.crossdev.onboarding1.data.dao.CurrencyDao
import com.lgcns.crossdev.onboarding1.data.dao.TravelDao
import com.lgcns.crossdev.onboarding1.data.database.AppDatabase
import com.lgcns.crossdev.onboarding1.data.datasource.local.CurrencyLocalDataSource
import com.lgcns.crossdev.onboarding1.data.datasource.remote.CurrencyRemoteDataSource
import com.lgcns.crossdev.onboarding1.data.datasource.local.TravelLocalDataSource
import com.lgcns.crossdev.onboarding1.data.network.CurrencyApiClient
import com.lgcns.crossdev.onboarding1.data.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class TravelRemoteDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class TravelLocalDataSource

    @Singleton
    @TravelLocalDataSource
    @Provides
    fun provideTravelLocalDataSource(
        travelDao: TravelDao
    ) = TravelLocalDataSource(
        travelDao
    )

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class CurrencyRemoteDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class CurrencyLocalDataSource

    @Singleton
    @CurrencyRemoteDataSource
    @Provides
    fun provideCurrencyRemoteDataSource(
        currencyApiClient: CurrencyApiClient
    ) = CurrencyRemoteDataSource(
        currencyApiClient
    )

    @Singleton
    @CurrencyLocalDataSource
    @Provides
    fun provideCurrencyLocalDataSource(
        currencyDao: CurrencyDao
    ) = CurrencyLocalDataSource(
        currencyDao
    )

    @Singleton
    @Provides
    fun provideTravelDao(appDatabase: AppDatabase) = appDatabase.travelDao()

    @Singleton
    @Provides
    fun provideCurrencyDao(appDatabase: AppDatabase) = appDatabase.currencyDao()

    @Singleton
    @Provides
    fun provideCurrencyApiClient() = CurrencyApiClient.create()


    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        Constants.DATABASE_NAME
    ).build()

}