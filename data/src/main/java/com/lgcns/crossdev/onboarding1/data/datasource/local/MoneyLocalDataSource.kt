package com.lgcns.crossdev.onboarding1.data.datasource.local

import com.lgcns.crossdev.onboarding1.data.dao.MoneyDao
import com.lgcns.crossdev.onboarding1.data.entity.MoneyEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class MoneyLocalDataSource @Inject constructor(
    private val moneyDao: MoneyDao,
) {
    suspend fun insertMoney(money: MoneyEntity): Long {
        return moneyDao.insertMoney(money)
    }
    suspend fun deleteMoney(money: MoneyEntity) {
        return moneyDao.deleteMoney(money)
    }
//    suspend fun updatePlanPos(id: Long, pos: Int) {
//        return planDao.updatePlanPos(id, pos)
//    }
    fun getMoneysByDate(travelId: Long, date: LocalDate?): Flow<List<MoneyEntity>> {
        return if(date == null) {
            moneyDao.getAllMoneys(travelId)
        }
        else {
            moneyDao.getMoneysByDate(travelId, date)
        }
    }
}