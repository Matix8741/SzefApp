package com.example.szefapp.logic.service

import android.content.SharedPreferences
import androidx.core.content.edit


object PreferenceService {

    private val POINTS = "POINTS";

    private lateinit var sharedPreference: SharedPreferences

    fun addPoints(points: Int) {
        val newPoints = sharedPreference.getInt(POINTS, 0) + points
        sharedPreference.edit { putInt(POINTS, newPoints) }
    }
}