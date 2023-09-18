package com.inlay.hotelroomservice.presentation.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.inlay.hotelroomservice.databinding.ActivitySplashBinding
import com.inlay.hotelroomservice.presentation.viewmodels.splash.SplashViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        val intent = Intent(this@SplashActivity, MainActivity::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            startActivity(intent)
            finish()
        } else {

            lifecycleScope.launch {
                viewModel.nightModeState.collect {
                    val currentNightMode =
                        resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                    if (it != currentNightMode) {

                        if (it == AppCompatDelegate.MODE_NIGHT_YES) AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_YES
                        )
                        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    } else {
                        val newNightMode = when (currentNightMode) {
                            Configuration.UI_MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_NO
                            Configuration.UI_MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_YES
                            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                        }
                        AppCompatDelegate.setDefaultNightMode(newNightMode)
                    }
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                delay(1000)
                startActivity(intent)
                finish()
            }
        }
    }
}