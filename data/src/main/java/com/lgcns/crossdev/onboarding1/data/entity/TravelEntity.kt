package com.lgcns.crossdev.onboarding1.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "travel")
data class TravelEntity(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val title: String,
    val dateStart: LocalDate,
    val dateEnd: LocalDate,
    val img: String?
)