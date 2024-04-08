package com.lgcns.crossdev.onboarding1.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lgcns.crossdev.onboarding1.data.entity.CurrencyEntity
import com.lgcns.crossdev.onboarding1.data.entity.TravelEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currency")
    fun getAllCurrency(): Flow<List<CurrencyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencies: List<CurrencyEntity>)
}