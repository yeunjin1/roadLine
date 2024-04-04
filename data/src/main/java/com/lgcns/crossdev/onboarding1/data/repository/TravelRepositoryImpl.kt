package com.lgcns.crossdev.onboarding1.data.repository

import com.lgcns.crossdev.onboarding1.data.datasource.local.TravelLocalDataSource
import com.lgcns.crossdev.onboarding1.data.mapper.Mapper
import com.lgcns.crossdev.onboarding1.domain.model.Travel
import com.lgcns.crossdev.onboarding1.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TravelRepositoryImpl @Inject constructor(
    private val travelLocalDataSource: TravelLocalDataSource
): TravelRepository {
    override fun getAllTravels(): Flow<List<Travel>> = travelLocalDataSource.getAllTravels().map { entityList ->
        entityList.map { entity -> Mapper.mapperTravelDomain(entity) }
    }

    override fun getTravelById(travelId: Long): Flow<Travel> = travelLocalDataSource.getTravelById(travelId).map {entity ->
        Mapper.mapperTravelDomain(entity)
    }

    override suspend fun insertTravel(travel: Travel): Long = travelLocalDataSource.insertTravel(Mapper.mapperTravelEntity(travel))

    override suspend fun deleteTravel(travel: Travel) = travelLocalDataSource.deleteTravel(Mapper.mapperTravelEntity(travel))

    override suspend fun updateTravel(travel: Travel) = travelLocalDataSource.updateTravel(Mapper.mapperTravelEntity(travel))
}