package com.example.starwars.model;

import com.google.gson.annotations.SerializedName;

public class Crew {
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private int id;
    @SerializedName("logo_path")
    private String logoPath;
    @SerializedName("origin_country")
    private String country;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
