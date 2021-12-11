package com.example.starwars.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.starwars.R
import android.content.Intent
import com.example.starwars.activity.FilmListActivity

// TODO Rename please to SplashActivity
class ThemeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO it's not necessary to apply theme programmatically without special condition, you can do it in manifest to concrete activity
        setTheme(R.style.Theme_SplashTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme)
        // TODO not good way to block, look in way of Handler().postDelayed
        try {
            Thread.sleep(2400)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        startActivity(Intent(this, FilmListActivity::class.java))
    }
}