package com.lgcns.crossdev.onboarding1.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "currency")
data class CurrencyEntity(
    @PrimaryKey val code: String,
    val name: String,
    val rate: Double?,
//    val symbol: String
)