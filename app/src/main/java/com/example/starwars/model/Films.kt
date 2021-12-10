package com.example.starwars.model

import com.google.gson.annotations.SerializedName
import com.example.starwars.model.Film

class Films {
    @SerializedName("results")
    var films: List<Film>? = null
        private set

    @SerializedName("total_pages")
    val pages = 0

    constructor() {}
    constructor(results: List<Film>?) {
        films = results
    }
}