package com.example.szefapp.persistence.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.szefapp.logic.vos.DayOfTheWeek
import com.example.szefapp.logic.vos.TaskValue
import java.util.*


@Entity(tableName = "Task")
data class TaskEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "text")
    val text: String,
    @ColumnInfo(name = "isDone")
    var isDone: Boolean,
    @ColumnInfo(name = "creationTime")
    val creationDate: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "repeatDays")
    val daysOfRepeat: List<DayOfTheWeek> = emptyList(),
    @ColumnInfo(name = "modificationTime")
    var lastDoneDate: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "value")
    val value: TaskValue = TaskValue.EASY
)