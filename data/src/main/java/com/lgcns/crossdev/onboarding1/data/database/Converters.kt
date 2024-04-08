package com.lgcns.crossdev.onboarding1.data.database

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.lgcns.crossdev.onboarding1.data.entity.CurrencyEntity
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

    @TypeConverter
    fun listToJson(value: List<String>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<String>? {
        return Gson().fromJson(value, Array<String>::class.java)?.toList()
    }
}