package com.lgcns.crossdev.onboarding1.data.repository

import com.lgcns.crossdev.onboarding1.data.datasource.local.PlanLocalDataSource
import com.lgcns.crossdev.onboarding1.data.mapper.toEntity
import com.lgcns.crossdev.onboarding1.data.mapper.toModel
import com.lgcns.crossdev.onboarding1.domain.model.Plan
import com.lgcns.crossdev.onboarding1.domain.repository.PlanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlanRepositoryImpl @Inject constructor(
    private val planLocalDataSource: PlanLocalDataSource
): PlanRepository {
    override suspend fun insertPlan(plan: Plan): Long = planLocalDataSource.insertPlan(plan.toEntity())
    override suspend fun deletePlan(plan: Plan) = planLocalDataSource.deletePlan(plan.toEntity())
    override suspend fun updatePlanPos(id: Long, pos: Int) = planLocalDataSource.updatePlanPos(id, pos)

    override fun getPlansByDate(travelId: Long, date: LocalDate?) = planLocalDataSource.getPlansByDate(travelId, date).map { entityList ->
        entityList.map { entity -> entity.toModel() }
    }
}