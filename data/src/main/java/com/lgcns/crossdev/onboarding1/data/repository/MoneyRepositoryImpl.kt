package com.lgcns.crossdev.onboarding1.data.repository

import com.lgcns.crossdev.onboarding1.data.datasource.local.MoneyLocalDataSource
import com.lgcns.crossdev.onboarding1.data.datasource.local.PlanLocalDataSource
import com.lgcns.crossdev.onboarding1.data.mapper.toEntity
import com.lgcns.crossdev.onboarding1.data.mapper.toModel
import com.lgcns.crossdev.onboarding1.domain.model.Money
import com.lgcns.crossdev.onboarding1.domain.model.Plan
import com.lgcns.crossdev.onboarding1.domain.repository.MoneyRepository
import com.lgcns.crossdev.onboarding1.domain.repository.PlanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoneyRepositoryImpl @Inject constructor(
    private val moneyLocalDataSource: MoneyLocalDataSource
): MoneyRepository {
    override fun getMoneysByDate(travelId: Long, date: LocalDate?): Flow<List<Money>> = moneyLocalDataSource.getMoneysByDate(travelId, date).map { entityList ->
        entityList.map { entity -> entity.toModel() }
    }

    override suspend fun insertMoney(money: Money): Long = moneyLocalDataSource.insertMoney(money.toEntity())
    override suspend fun deleteMoney(money: Money) = moneyLocalDataSource.deleteMoney(money.toEntity())
//
//    override suspend fun insertPlan(plan: Plan): Long = planLocalDataSource.insertPlan(plan.toEntity())
//    override suspend fun deletePlan(plan: Plan) = planLocalDataSource.deletePlan(plan.toEntity())
//    override suspend fun updatePlanPos(id: Long, pos: Int) = planLocalDataSource.updatePlanPos(id, pos)
}