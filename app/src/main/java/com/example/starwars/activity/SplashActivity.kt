package com.example.starwars.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.starwars.R
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.starwars.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.logoIv.setImageResource(R.drawable.logo)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, FilmListActivity::class.java))
        }, 2400)

    }
}