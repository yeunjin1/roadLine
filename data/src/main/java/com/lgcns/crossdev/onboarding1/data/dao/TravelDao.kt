package com.lgcns.crossdev.onboarding1.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lgcns.crossdev.onboarding1.data.entity.TravelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TravelDao {
    @Query("SELECT * FROM travel")
    fun getAllTravels(): Flow<List<TravelEntity>>

    @Query("SELECT * FROM travel WHERE id = :travelId")
    fun getTravelById(travelId: Long): Flow<TravelEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTravel(travel: TravelEntity): Long

    @Delete
    suspend fun deleteTravel(travel: TravelEntity)

    @Update
    suspend fun updateTravel(travel: TravelEntity)

//    @Transaction
//    @Query("SELECT * FROM travel WHERE id = :travelId")
//    fun getTravelWithDays(travelId: Int): LiveData<TravelWithDays>
//
//    @Transaction
//    @Query("SELECT * FROM travel WHERE id = :travelId")
//    fun getTravelWithDaysAndPlans(travelId: Int): LiveData<TravelWithDaysAndPlans>

}