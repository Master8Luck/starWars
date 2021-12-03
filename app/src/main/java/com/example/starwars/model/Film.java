package com.example.starwars.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Film {
    @SerializedName("title")
    private String title;
    @SerializedName("episode_id")
    private int episodeId;
    @SerializedName("opening_crawl")
    private String openingCrawl;
    @SerializedName("release_date")
    private String releaseDate;

    public Film(String title, int episode_id, String opening_crawl, String release_date) {
        this.title = title;
        this.episodeId = episode_id;
        this.openingCrawl = opening_crawl;
        this.releaseDate = release_date;
    }

    public Film() {
    }

    public String getFullTitle() {
        return title + " " + episodeId;
    }

    public String getOpeningCrawl() {
        return openingCrawl.length() > 10 ? openingCrawl.substring(0, 10) : openingCrawl;
    }

    @Override
    public String toString() {
        return "Film{" +
                "title='" + title + '\'' +
                ", episode_id=" + episodeId +
                ", opening_crawl='" + openingCrawl + '\'' +
                ", release_date='" + releaseDate + '\'' +
                '}';
    }
}
