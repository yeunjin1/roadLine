package com.lgcns.crossdev.onboarding1.data.datasource.local

import com.lgcns.crossdev.onboarding1.data.dao.TravelDao
import com.lgcns.crossdev.onboarding1.data.entity.TravelEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TravelLocalDataSource @Inject constructor(
    private val travelDao: TravelDao
) {
    fun getAllTravels(): Flow<List<TravelEntity>> {
        return travelDao.getAllTravels()
    }

    fun getTravelById(travelId: Long): Flow<TravelEntity> {
        return travelDao.getTravelById(travelId)
    }

    suspend fun insertTravel(travel: TravelEntity): Long {
        return travelDao.insertTravel(travel)
    }

    suspend fun deleteTravel(travel: TravelEntity) {
        return travelDao.deleteTravel(travel)
    }

    suspend fun updateTravel(travel: TravelEntity) {
        return travelDao.updateTravel(travel)
    }
}