package com.lgcns.crossdev.onboarding1.test.repository

import com.lgcns.crossdev.onboarding1.test.model.Money
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface MoneyRepository {
    suspend fun insertMoney(money: Money): Long
    suspend fun deleteMoney(money: Money)
    fun getMoneysByDate(travelId: Long, date: LocalDate?): Flow<List<Money>>
}