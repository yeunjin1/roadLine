package com.lgcns.crossdev.onboarding1.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lgcns.crossdev.onboarding1.data.entity.PlanEntity
import com.lgcns.crossdev.onboarding1.data.entity.TravelEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface PlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: PlanEntity): Long

    @Delete
    suspend fun deletePlan(plan: PlanEntity)

    @Query("UPDATE `plan` SET pos = :pos WHERE id = :id ")
    suspend fun updatePlanPos(id: Long, pos: Int)

    @Query("SELECT * FROM `plan` WHERE travelId = :travelId AND date = :date ORDER BY pos")
    fun getPlansByDate(travelId: Long, date: LocalDate): Flow<List<PlanEntity>>

    @Query("SELECT * FROM `plan` WHERE travelId = :travelId ORDER BY date, pos")
    fun getAllPlans(travelId: Long): Flow<List<PlanEntity>>
}