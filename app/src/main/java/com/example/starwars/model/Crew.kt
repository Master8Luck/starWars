package com.example.starwars.model

import com.google.gson.annotations.SerializedName

class Crew {
    @SerializedName("name")
    var name: String? = null

    @SerializedName("id")
    var id = 0

    @SerializedName("logo_path")
    var logoPath: String? = null

    @SerializedName("origin_country")
    var country: String? = null
}