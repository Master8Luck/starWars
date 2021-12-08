package com.example.starwars.model;

import com.google.gson.annotations.SerializedName;

public class Genre {
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }
    public static String convertToString(Genre[] genres) {
        StringBuilder builder = new StringBuilder();
        for (Genre genre: genres) {
            builder.append(genre.getName()).append(", ");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}

