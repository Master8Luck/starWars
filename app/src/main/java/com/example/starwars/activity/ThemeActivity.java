package com.example.starwars.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.starwars.R;

public class ThemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_SplashTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(this, FilmListActivity.class));
    }
}