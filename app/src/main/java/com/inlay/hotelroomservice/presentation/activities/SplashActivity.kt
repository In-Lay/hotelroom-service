package com.inlay.hotelroomservice.presentation.activities

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import com.inlay.hotelroomservice.R
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
        val intent = Intent(this@SplashActivity, MainActivity::class.java)

        //TODO Transition between Activities doesn't work
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        val transitionInflater = TransitionInflater.from(this)
        window.exitTransition = transitionInflater.inflateTransition(R.transition.fade_long)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val bundle =
                ActivityOptionsCompat.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out)
                    .toBundle()
            startActivity(intent, bundle)
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, R.anim.fade_in, R.anim.fade_out)
//            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        } else {
            binding = ActivitySplashBinding.inflate(layoutInflater)
            binding.lifecycleOwner = this
            setContentView(binding.root)

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
//                finish()
                ActivityCompat.finishAfterTransition(this@SplashActivity)
            }
        }
    }
}