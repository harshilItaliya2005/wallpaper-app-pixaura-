package com.example.pixaura.ui.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.pixaura.R
import com.example.pixaura.databinding.ActivityMainBinding
import com.example.pixaura.ui.activity.loginScreen.LoginActivity

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var sharedPref: SharedPreferences
    private var isAnimationDone = false
    private var isProgressDone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)

        startSplashAnimation()
    }

    private fun startSplashAnimation() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                binding.logoImage.alpha = 1f
                binding.welcomeText.alpha = 1f
                binding.subheadingText.alpha = 1f
                isAnimationDone = true
                checkReadyToNavigate()
            }
        })

        binding.logoImage.startAnimation(fadeIn)
        binding.welcomeText.startAnimation(fadeIn)
        binding.subheadingText.startAnimation(fadeIn)

        animateProgress()
    }

    private fun animateProgress() {
        val progressBar = binding.loadingBar
        progressBar.setMax(100f)

        var progress = 0f
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                if (progress <= 100f) {
                    progressBar.setProgress(progress)
                    progress += 5f
                    handler.postDelayed(this, 25)
                } else {
                    isProgressDone = true
                    checkReadyToNavigate()
                }
            }
        }
        handler.postDelayed(runnable, 200)
    }

    private fun checkReadyToNavigate() {
        if (isAnimationDone && isProgressDone) {
            val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)
            val target =
                if (isLoggedIn) MainScreenActivity::class.java else LoginActivity::class.java
            startActivity(Intent(this, target))
            finish()
        }
    }


}
