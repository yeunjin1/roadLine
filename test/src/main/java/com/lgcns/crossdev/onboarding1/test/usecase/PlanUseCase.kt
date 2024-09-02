package com.lgcns.crossdev.onboarding1.test.usecase

import com.lgcns.crossdev.onboarding1.test.model.Plan
import com.lgcns.crossdev.onboarding1.test.repository.PlanRepository
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsertPlanUseCase @Inject constructor(
    private val planRepository: PlanRepository
) {
    suspend operator fun invoke(plan: Plan) = planRepository.insertPlan(plan)
}

@Singleton
class GetPlansByDateUseCase @Inject constructor(
    private val planRepository: PlanRepository
) {
    operator fun invoke(travelId: Long, date: LocalDate?) = planRepository.getPlansByDate(travelId, date)
}

@Singleton
class DeletePlanUseCase @Inject constructor(
    private val planRepository: PlanRepository
) {
    suspend operator fun invoke(plan: Plan) = planRepository.deletePlan(plan)
}

@Singleton
class UpdatePlanPosUseCase @Inject constructor(
    private val planRepository: PlanRepository
) {
    suspend operator fun invoke(id: Long, pos: Int) = planRepository.updatePlanPos(id, pos)
}