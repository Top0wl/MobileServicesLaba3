package com.example.mobileserviceslaba3
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var checkbox: CheckBox
    private lateinit var progressBar: ProgressBar
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var getTimestampButton: Button
    private lateinit var bindButton: Button
    private lateinit var unbindButton: Button
    private lateinit var outputTextView: TextView

    private val handler = Handler(Looper.getMainLooper())
    private var isServiceRunning = false
    private var isServiceBound = false
    private var timestampService: TimestampService? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as TimestampService.TimestampBinder
            timestampService = binder.getService()
            Log.d("TimestampService", "Service connected")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            timestampService = null
            Log.d("TimestampService", "Service disconnected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkbox = findViewById(R.id.checkbox)
        progressBar = findViewById(R.id.progressBar)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        getTimestampButton = findViewById(R.id.getTimestampButton)
        bindButton = findViewById(R.id.bindButton)
        unbindButton = findViewById(R.id.unbindButton)
        outputTextView = findViewById(R.id.outputTextView)

        startButton.setOnClickListener {
            startService()
        }

        stopButton.setOnClickListener {
            stopService()
        }

        getTimestampButton.setOnClickListener {
            getTimestamp()
        }

        bindButton.setOnClickListener {
            bindService()
        }

        unbindButton.setOnClickListener {
            unbindService()
        }

        checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startService()
            } else {
                stopService()
            }
        }
    }

    private fun startService() {
        if (!isServiceRunning) {
            val intentService = Intent(this, MyIntentService::class.java)
            startService(intentService)
            isServiceRunning = true

            // Обновление прогресс-бара каждую секунду
            handler.postDelayed(object : Runnable {
                override fun run() {
                    if (isServiceRunning) {
                        updateProgressBar()
                        handler.postDelayed(this, 1000)
                    }
                }
            }, 1000)
        }
    }

    private fun stopService() {
        if (isServiceRunning) {
            stopService(Intent(this, MyIntentService::class.java))
            isServiceRunning = false
            progressBar.progress = 0
            handler.removeCallbacksAndMessages(null)
        }
    }

    private fun updateProgressBar() {
        progressBar.incrementProgressBy(1)
    }

    private fun bindService() {
        if (!isServiceBound) {
            val intent = Intent(this, TimestampService::class.java)
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
            isServiceBound = true

            // Логирование в текстовое поле
            outputTextView.text = "Service bound"
        }
    }

    private fun unbindService() {
        if (isServiceBound) {
            unbindService(connection)
            isServiceBound = false

            // Логирование в текстовое поле
            outputTextView.text = "Service unbound"
        }
    }

    private fun getTimestamp() {
        if (isServiceBound) {
            val timestamp = timestampService?.getTimestamp() ?: "Service not bound"
            outputTextView.text = "Timestamp: $timestamp"
        } else {
            outputTextView.text = "Service not bound"
        }
    }
}