package com.example.mobileserviceslaba3

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.text.SimpleDateFormat
import java.util.*

class TimestampService : Service() {

    private val binder = TimestampBinder()
    private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    inner class TimestampBinder : Binder() {
        fun getService(): TimestampService = this@TimestampService
    }

    fun getTimestamp(): String {
        return dateFormat.format(Date())
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }
}