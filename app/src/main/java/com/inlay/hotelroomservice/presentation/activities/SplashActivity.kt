package com.inlay.hotelroomservice.presentation.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.inlay.hotelroomservice.databinding.ActivitySplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            startActivity(intent)
        }

        //TODO Check for night mode plus request notification and location permissions
        Log.d("SplashTag", "onCreate")
        binding = ActivitySplashBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            startActivity(intent)
            finish()
        }
    }
}