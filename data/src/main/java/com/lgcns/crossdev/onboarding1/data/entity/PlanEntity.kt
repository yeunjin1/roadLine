package com.lgcns.crossdev.onboarding1.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.time.LocalDate

@Entity(tableName = "plan",
    foreignKeys = [ForeignKey(
        entity = TravelEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("travelId"),
        onDelete = ForeignKey.CASCADE
    )])
data class PlanEntity (
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val travelId: Long,
    val date: LocalDate,
    val name: String,
    val nameAlter: String?,
    val locationX: Double,
    val locationY: Double,
    val time: Int?,
    val memo: String?,
    val pos: Int = 0
)