package com.example.starwars

import android.content.Context
import android.net.ConnectivityManager

object ConnectionUtils {
    fun isInternetConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }
}

object API_ENDPOINTS {
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342/"
    const val API_BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGE_CREW_BASE_URL = "https://image.tmdb.org/t/p/w300/"
    const val API_KEY = "430dc4bcb90f3bd8e2616b75a712749c"
}

