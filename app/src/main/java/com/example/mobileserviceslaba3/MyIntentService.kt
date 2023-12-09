package com.example.mobileserviceslaba3

import android.app.IntentService
import android.content.Intent
import android.util.Log

class MyIntentService : IntentService("MyIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        // Эмулируем долгую операцию (например, сон на 5 секунд)
        try {
            for (i in 0..4) {
                Thread.sleep(1000)
                Log.d("MyIntentService", "Progress: ${i + 1} seconds")
            }
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }
}