package com.lgcns.crossdev.onboarding1.test.repository

import com.lgcns.crossdev.onboarding1.test.model.Plan
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface PlanRepository {
    suspend fun insertPlan(plan: Plan): Long
    suspend fun deletePlan(plan: Plan)
    suspend fun updatePlanPos(id: Long, pos: Int)
    fun getPlansByDate(travelId: Long, date: LocalDate?): Flow<List<Plan>>
}