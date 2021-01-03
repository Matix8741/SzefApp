package com.example.szefapp.persistence.task

import androidx.room.TypeConverter
import com.example.szefapp.logic.vos.DayOfTheWeek
import com.example.szefapp.logic.vos.TaskValue

class TaskConverter {
    @TypeConverter
    fun daysOfTheWeekToString(daysOfTheWeek: List<DayOfTheWeek>): String {
        return daysOfTheWeek.joinToString(separator = ",")
    }

    @TypeConverter
    fun stringToDaysOfTheWeek(daysOfTheWeek: String): List<DayOfTheWeek> {
        return daysOfTheWeek.split(",").map { DayOfTheWeek.valueOf(it) }
    }

    @TypeConverter
    fun taskValueToInteger(taskValue: TaskValue): Int {
        return taskValue.value
    }

    @TypeConverter
    fun integerToTaskValue(taskValue: Int): TaskValue {
        return when (taskValue) {
            TaskValue.EASY.value -> TaskValue.EASY
            TaskValue.MIDIUM.value -> TaskValue.MIDIUM
            TaskValue.HARD.value -> TaskValue.HARD
            else -> {
                TaskValue.EASY
            }
        }
    }
}