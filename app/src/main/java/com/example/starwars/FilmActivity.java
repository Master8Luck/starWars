package com.example.starwars;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.starwars.model.Film;

import java.util.List;

public class FilmActivity extends Activity {

    TextView filmNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_StarWars);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);

        filmNameTextView = findViewById(R.id.film_name);

        filmNameTextView.setText(getIntent().getStringExtra(MainActivity.FILMS_TAG));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}