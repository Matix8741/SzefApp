package com.example.szefapp.persistence.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "Task")
data class TaskEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "text")
    val text: String,
    @ColumnInfo(name = "refreshTimer")
    val refreshTimer: Int,
    @ColumnInfo(name = "isDone")
    var isDone: Boolean,
    @ColumnInfo(name = "creationTime")
    val creationTime: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "modificationTime")
    var modificationTime: Long = System.currentTimeMillis()
)