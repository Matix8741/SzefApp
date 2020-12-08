package com.example.szefapp.logic.vos

import androidx.room.ColumnInfo
import java.util.*


class TaskVo {
    private lateinit var id: String
    private lateinit var text: String
    private  var refreshTimer = 0
    private  var isDone = false
}