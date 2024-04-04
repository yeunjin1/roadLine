package com.lgcns.crossdev.onboarding1.domain.usecase

import com.lgcns.crossdev.onboarding1.domain.model.Travel
import com.lgcns.crossdev.onboarding1.domain.repository.TravelRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllTravelsUseCase @Inject constructor(
    private val travelRepository: TravelRepository
) {
    operator fun invoke() = travelRepository.getAllTravels()
}

@Singleton
class GetTravelUseCase @Inject constructor(
    private val travelRepository: TravelRepository
) {
    operator fun invoke(travelId: Long) = travelRepository.getTravelById(travelId)
}

@Singleton
class InsertTravelUseCase @Inject constructor(
    private val travelRepository: TravelRepository
) {
    suspend operator fun invoke(travel: Travel) = travelRepository.insertTravel(travel)
}

@Singleton
class DeleteTravelUseCase @Inject constructor(
    private val travelRepository: TravelRepository
) {
    suspend operator fun invoke(travel: Travel) = travelRepository.deleteTravel(travel)
}

@Singleton
class UpdateTravelUseCase @Inject constructor(
    private val travelRepository: TravelRepository
) {
    suspend operator fun invoke(travel: Travel) = travelRepository.updateTravel(travel)
}