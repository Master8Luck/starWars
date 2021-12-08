package com.example.starwars.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.starwars.adapter.FilmsAdapter;
import com.example.starwars.R;
import com.example.starwars.model.Film;
import com.example.starwars.viewmodel.FilmListActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class FilmListActivity extends AppCompatActivity implements FilmsAdapter.FilmClickListener {

    public static final String FILM_ID_EXTRA_KEY = "films";
    public static final String TAG = "asd";
    public static final String ERROR_MESSAGE = "No result in cache. Please connect to the internet";
    public static final String CACHE_MESSAGE = "No internet connection. Showing cache";

    private FilmListActivityViewModel mViewModel;
    private FilmsAdapter mAdapter;
    private RecyclerView recyclerView;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_list);

        mViewModel = ViewModelProviders.of(this).get(FilmListActivityViewModel.class);
        mViewModel.init();
        mViewModel.getFilmsLiveData().observe(this, new Observer<List<Film>>() {
            @Override
            public void onChanged(List<Film> films) {
                if (films == null || films.isEmpty()) {
                    showErrorMessage();
                } else if (!isInternetConnected()) {
                    showCacheMessage();
                }
                mAdapter.setFilms((ArrayList<Film>) films);
            }
        });

        mViewModel.getIndicatorLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                } else {
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

        recyclerView = findViewById(R.id.rv_films);
        loadingProgressBar = findViewById(R.id.pb_loading_indicator);
        mAdapter = new FilmsAdapter(new ArrayList<>(), this, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    mViewModel.loadData();
                }
            }
        });


    }

    private void showErrorMessage(){
        Toast.makeText(this, ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
    }

    private void showCacheMessage(){
        Toast.makeText(this, CACHE_MESSAGE, Toast.LENGTH_SHORT).show();
    }

    private boolean isInternetConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    @Override
    public void onItemClick(int position) {
        Intent filmActivityIntent = new Intent(this, FilmActivity.class);
        filmActivityIntent.putExtra(FILM_ID_EXTRA_KEY, mViewModel.getFilmsLiveData().getValue().get(position).getId());
        startActivity(filmActivityIntent);
    }
}