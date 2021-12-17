package com.example.starwars.domain.model

import com.google.gson.annotations.SerializedName

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