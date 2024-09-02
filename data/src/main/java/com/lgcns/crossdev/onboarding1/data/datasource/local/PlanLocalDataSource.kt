package com.lgcns.crossdev.onboarding1.data.datasource.local

import com.lgcns.crossdev.onboarding1.data.dao.PlanDao
import com.lgcns.crossdev.onboarding1.data.entity.PlanEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class PlanLocalDataSource @Inject constructor(
    private val planDao: PlanDao,
) {
    suspend fun insertPlan(plan: PlanEntity): Long {
        return planDao.insertPlan(plan)
    }
    suspend fun deletePlan(plan: PlanEntity) {
        return planDao.deletePlan(plan)
    }
    suspend fun updatePlanPos(id: Long, pos: Int) {
        return planDao.updatePlanPos(id, pos)
    }
    fun getPlansByDate(travelId: Long, date: LocalDate?): Flow<List<PlanEntity>> {
        return if(date == null) {
            planDao.getAllPlans(travelId)
        }
        else {
            planDao.getPlansByDate(travelId, date)
        }
    }
}