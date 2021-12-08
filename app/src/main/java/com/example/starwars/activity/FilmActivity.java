package com.example.starwars.activity;

import static com.example.starwars.activity.FilmListActivity.ERROR_MESSAGE;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.starwars.R;
import com.example.starwars.StarWarsApp;
import com.example.starwars.model.Film;
import com.example.starwars.model.Genre;
import com.example.starwars.viewmodel.FilmActivityViewModel;

public class FilmActivity extends AppCompatActivity {

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342/";

    private FilmActivityViewModel mViewModel;

    private ImageView mFilmIcon;
    private TextView mFilmTitleTextView;
    private TextView mFilmDateTextView;
    private TextView mFilmGenresTextView;
    private TextView mFilmOverviewTextView;
    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_StarWars);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);

        mFilmIcon = findViewById(R.id.film_icon);
        mFilmTitleTextView = findViewById(R.id.film_title);
        mFilmDateTextView = findViewById(R.id.film_date);
        mFilmGenresTextView = findViewById(R.id.film_genres);
        mFilmOverviewTextView = findViewById(R.id.film_overview);

        mId = getIntent().getIntExtra(FilmListActivity.FILM_ID_EXTRA_KEY, 0);
        mViewModel = ViewModelProviders.of(this).get(FilmActivityViewModel.class);
        mViewModel.init(mId);
        mViewModel.getFilmLiveData().observe(this, new Observer<Film>() {
            @Override
            public void onChanged(Film film) {
                setData(film);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setData(Film film) {
        if (film != null) {
            Glide.with(StarWarsApp.getContext())
                    .load(IMAGE_BASE_URL + film.getPosterPath())
                    .error(R.drawable.ic_launcher_background)
                    .into(mFilmIcon);
            mFilmTitleTextView.setText(film.getTitle());
            mFilmDateTextView.setText(film.getReleaseDate());
            mFilmGenresTextView.setText(Genre.convertToString(film.getGenres()));
            mFilmOverviewTextView.setText(film.getOverview());
        } else {
            Toast.makeText(this, ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }
}