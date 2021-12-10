package com.example.starwars

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.starwars.StarWarsApp

class StarWarsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
            private set
    }
}