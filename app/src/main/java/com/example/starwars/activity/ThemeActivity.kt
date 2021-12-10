package com.example.starwars.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.starwars.R
import android.content.Intent
import com.example.starwars.activity.FilmListActivity

class ThemeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_SplashTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme)
        try {
            Thread.sleep(2400)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        startActivity(Intent(this, FilmListActivity::class.java))
    }
}