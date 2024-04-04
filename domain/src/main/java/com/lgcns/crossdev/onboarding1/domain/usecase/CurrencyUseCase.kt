package com.lgcns.crossdev.onboarding1.domain.usecase

import com.lgcns.crossdev.onboarding1.domain.repository.CurrencyRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadRemoteCurrencyUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    suspend operator fun invoke() = currencyRepository.loadRemoteCurrency()
}
