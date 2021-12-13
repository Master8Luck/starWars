package com.example.starwars

import android.content.Context
import android.net.ConnectivityManager

class ConnectionUtils {
    companion object {
        fun isInternetConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
        }
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342/"
        const val IMAGE_CREW_BASE_URL = "https://image.tmdb.org/t/p/w300/"
    }
}