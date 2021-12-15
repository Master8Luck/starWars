package com.example.starwars

import android.content.Context
import android.net.ConnectivityManager

// TODO you can avoid using companion in this class just transform 'class' to 'object' and remove companion object
class ConnectionUtils {
    companion object {
        fun isInternetConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
        }
        // TODO I think this constants should be not in this place, maybe create some object and called him API_ENDPOINTS and put it here
        // also put there our base api url "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342/"
        const val IMAGE_CREW_BASE_URL = "https://image.tmdb.org/t/p/w300/"
    }
}