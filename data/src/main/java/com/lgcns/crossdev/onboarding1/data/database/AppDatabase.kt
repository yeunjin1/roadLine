package com.lgcns.crossdev.onboarding1.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lgcns.crossdev.onboarding1.data.dao.CurrencyDao
import com.lgcns.crossdev.onboarding1.data.dao.MoneyDao
import com.lgcns.crossdev.onboarding1.data.dao.PlanDao
import com.lgcns.crossdev.onboarding1.data.dao.TravelDao
import com.lgcns.crossdev.onboarding1.data.entity.CurrencyEntity
import com.lgcns.crossdev.onboarding1.data.entity.MoneyEntity
import com.lgcns.crossdev.onboarding1.data.entity.PlanEntity
import com.lgcns.crossdev.onboarding1.data.entity.TravelEntity

@Database(entities = [TravelEntity::class, CurrencyEntity::class, PlanEntity::class, MoneyEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun travelDao(): TravelDao
    abstract fun currencyDao(): CurrencyDao
    abstract fun planDao(): PlanDao
    abstract fun moneyDao(): MoneyDao
}