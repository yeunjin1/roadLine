package com.lgcns.crossdev.onboarding1.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lgcns.crossdev.onboarding1.data.dao.CurrencyDao
import com.lgcns.crossdev.onboarding1.data.dao.TravelDao
import com.lgcns.crossdev.onboarding1.data.entity.CurrencyEntity
import com.lgcns.crossdev.onboarding1.data.entity.TravelEntity

@Database(entities = [TravelEntity::class, CurrencyEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun travelDao(): TravelDao
    abstract fun currencyDao(): CurrencyDao
}