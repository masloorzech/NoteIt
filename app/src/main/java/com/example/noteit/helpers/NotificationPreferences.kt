package com.example.noteit.helpers

import android.content.Context

object NotificationPreferences {
    private const val PREFS_NAME = "noteit_prefs"
    private const val KEY_NOTIFICATION_TIME = "notification_time"

    fun saveNotificationTime(context: Context, minutesBefore: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_NOTIFICATION_TIME, minutesBefore).apply()
    }

    fun getNotificationTime(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_NOTIFICATION_TIME, 10)
    }
}
