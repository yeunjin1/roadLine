package com.lgcns.crossdev.onboarding1.domain.usecase

import com.lgcns.crossdev.onboarding1.domain.model.Currency
import com.lgcns.crossdev.onboarding1.domain.model.Plan
import com.lgcns.crossdev.onboarding1.domain.model.Travel
import com.lgcns.crossdev.onboarding1.domain.repository.CurrencyRepository
import com.lgcns.crossdev.onboarding1.domain.repository.PlanRepository
import com.lgcns.crossdev.onboarding1.domain.repository.TravelRepository
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