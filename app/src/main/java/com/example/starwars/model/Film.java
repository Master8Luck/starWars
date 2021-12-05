package com.example.starwars.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Film {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("vote_average")
    private double voteAverage;
    @SerializedName("genre_ids")
    private int[] genres;
    @SerializedName("poster_path")
    private String posterPath;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int[] getGenres() {
        return genres;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Film() {
    }

    public Film(int id, String title, double voteAverage, int[] genres, String posterPath) {
        this.id = id;
        this.title = title;
        this.voteAverage = voteAverage;
        this.genres = genres;
        this.posterPath = posterPath;
    }
}
