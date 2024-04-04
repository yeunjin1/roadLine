package com.lgcns.crossdev.onboarding1.data.database

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class Converters {
    @TypeConverter
    fun stringToLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(dateString) }
    }

    @TypeConverter
    fun localDateToString(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun stringToLocalDateTime(dateTimeString: String?): LocalDateTime? {
        return dateTimeString?.let {  LocalDateTime.parse(dateTimeString) }
    }

    @TypeConverter
    fun localDateTimeToString(date: LocalDateTime?): String? {
        return date.toString()
    }
}