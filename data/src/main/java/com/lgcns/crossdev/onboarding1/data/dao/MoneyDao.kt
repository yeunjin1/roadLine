package com.lgcns.crossdev.onboarding1.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lgcns.crossdev.onboarding1.data.entity.MoneyEntity
import com.lgcns.crossdev.onboarding1.data.entity.PlanEntity
import com.lgcns.crossdev.onboarding1.data.entity.TravelEntity
import com.lgcns.crossdev.onboarding1.domain.model.Money
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface MoneyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoney(money: MoneyEntity): Long

    @Delete
    suspend fun deleteMoney(money: MoneyEntity)
//
//    @Query("UPDATE `plan` SET pos = :pos WHERE id = :id ")
//    suspend fun updatePlanPos(id: Long, pos: Int)

    @Query("SELECT * FROM `money` WHERE travelId = :travelId AND date = :date")
    fun getMoneysByDate(travelId: Long, date: LocalDate): Flow<List<MoneyEntity>>

    @Query("SELECT * FROM `money` WHERE travelId = :travelId ORDER BY date")
    fun getAllMoneys(travelId: Long): Flow<List<MoneyEntity>>
}