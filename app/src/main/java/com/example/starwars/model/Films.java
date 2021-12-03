package com.example.starwars.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Films {
    @SerializedName("results")
    private List<Film> films;

    public Films() {
    }

    public Films(List<Film> results) {
        this.films = results;
    }

    public List<Film> getFilms() {
        return films;
    }
}
