package com.lgcns.crossdev.onboarding1.roadline.di

import com.lgcns.crossdev.onboarding1.data.repository.CurrencyRepositoryImpl
import com.lgcns.crossdev.onboarding1.data.repository.TravelRepositoryImpl
import com.lgcns.crossdev.onboarding1.domain.repository.CurrencyRepository
import com.lgcns.crossdev.onboarding1.domain.repository.TravelRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindTravelRepository(
        impl: TravelRepositoryImpl
    ): TravelRepository

    @Binds
    fun bindCurrencyRepository(
        impl: CurrencyRepositoryImpl
    ): CurrencyRepository

}

