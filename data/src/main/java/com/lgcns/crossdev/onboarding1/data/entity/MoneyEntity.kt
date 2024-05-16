package com.lgcns.crossdev.onboarding1.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "money",
    foreignKeys = [ForeignKey(
        entity = TravelEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("travelId"),
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = CurrencyEntity::class,
        parentColumns = arrayOf("code"),
        childColumns = arrayOf("currencyCode"),
        onDelete = ForeignKey.CASCADE
    )])
data class MoneyEntity (
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val travelId: Long,
    val currencyCode: String,
    val img: String?,
    val price: Double,
    val korPrice: Double? = null,
    val category: Int,
    val date: LocalDate,
    val memo: String?,
)