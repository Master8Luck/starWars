package com.example.starwars.model

import com.google.gson.annotations.SerializedName
import com.example.starwars.model.Genre
import java.lang.StringBuilder

class Genre {
    @SerializedName("name")
    val name: String? = null

    companion object {
        fun convertToString(genres: Array<Genre>?): String {
            if (genres == null) return ""
            val builder = StringBuilder()
            for (genre in genres) {
                builder.append(genre.name).append(", ")
            }
            builder.deleteCharAt(builder.length - 1)
            builder.deleteCharAt(builder.length - 1)
            return builder.toString()
        }
    }
}