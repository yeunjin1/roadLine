package com.lgcns.crossdev.onboarding1.test.repository

import com.lgcns.crossdev.onboarding1.test.model.Travel
import kotlinx.coroutines.flow.Flow

interface TravelRepository {
    fun getAllTravels(): Flow<List<Travel>>
    fun getTravelById(travelId: Long): Flow<Travel>
    suspend fun insertTravel(travel: Travel): Long
    suspend fun deleteTravel(travel: Travel)
    suspend fun updateTravel(travel: Travel)
}