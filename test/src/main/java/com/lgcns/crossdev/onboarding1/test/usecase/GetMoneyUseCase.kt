package com.lgcns.crossdev.onboarding1.test.usecase

import com.lgcns.crossdev.onboarding1.test.model.Money
import com.lgcns.crossdev.onboarding1.test.repository.MoneyRepository
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMoneysByDateUseCase @Inject constructor(
    private val moneyRepository: MoneyRepository
) {
    operator fun invoke(travelId: Long, date: LocalDate?) = moneyRepository.getMoneysByDate(travelId, date)
}

@Singleton
class InsertMoneyUseCase @Inject constructor(
    private val moneyRepository: MoneyRepository
) {
    suspend operator fun invoke(money: Money) = moneyRepository.insertMoney(money)
}

@Singleton
class DeleteMoneyUseCase @Inject constructor(
    private val moneyRepository: MoneyRepository
) {
    suspend operator fun invoke(money: Money) = moneyRepository.deleteMoney(money)
}

