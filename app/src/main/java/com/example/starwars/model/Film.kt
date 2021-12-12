package com.example.starwars.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import androidx.room.PrimaryKey
import com.example.starwars.model.Genre
import com.example.starwars.model.Crew
import io.reactivex.rxjava3.annotations.Nullable

@Entity
class Film {
    @SerializedName("id")
    @PrimaryKey
    var id = 0

    @SerializedName("title")
    var title: String? = null

    @SerializedName("vote_average")
    var voteAverage = 0.0

    @SerializedName("poster_path")
    var posterPath: String? = null

    @SerializedName("genres")
    var genres: @Nullable Array<Genre>? = null

    @SerializedName("overview")
    var overview: @Nullable String? = null

    @SerializedName("release_date")
    var releaseDate: @Nullable String? = null

    @SerializedName("production_companies")
    var crews: @Nullable MutableList<Crew>? = null
}