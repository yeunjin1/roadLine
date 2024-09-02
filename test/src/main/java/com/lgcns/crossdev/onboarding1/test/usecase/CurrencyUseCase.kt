package com.lgcns.crossdev.onboarding1.test.usecase

import com.lgcns.crossdev.onboarding1.test.model.Currency
import com.lgcns.crossdev.onboarding1.test.repository.CurrencyRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadRemoteCurrencyUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    suspend operator fun invoke(): Result<List<Currency>> = currencyRepository.loadRemoteCurrency()
}

@Singleton
class InsertCurrencyUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    suspend operator fun invoke(currencies: List<Currency>) {
        currencyRepository.deleteCurrencies()
        currencyRepository.insertCurrencies(currencies)
        currencyRepository.putLatestCurrencyLoadDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
    }
}

@Singleton
class GetLatestCurrencyLoadDateUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    operator fun invoke() = currencyRepository.getLatestCurrencyLoadDate()
}


@Singleton
class GetAllCurrenciesUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    operator fun invoke() = currencyRepository.getAllCurrencies()
}

@Singleton
class GetCurrenciesByCodeUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    operator fun invoke(code: List<String>) = currencyRepository.getCurrenciesByCode(code)
}
