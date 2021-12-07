package com.example.starwars.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.example.starwars.model.Films;
import com.example.starwars.retrofit.RetrofitClient;
import com.example.starwars.retrofit.StarAPI;
import com.example.starwars.viewmodel.FilmListActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;

public class FilmListActivity extends AppCompatActivity implements FilmsAdapter.FilmClickListener {

    public static final String FILMS_TAG = "films";
    public static final String TAG = "asd";
    public static final String ERROR_MESSAGE = "Failed to get results. Please try again later";

    FilmListActivityViewModel mViewModel;
    FilmsAdapter mAdapter;
    RecyclerView recyclerView;
    ProgressBar loadingProgressBar;
    List<Film> filmList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewModel = ViewModelProviders.of(this).get(FilmListActivityViewModel.class);
        mViewModel.init();
        mViewModel.getFilms().observe(this, new Observer<List<Film>>() {
            @Override
            public void onChanged(List<Film> films) {
                if (films == null) {
                    showErrorMessage();
                } else {
                    mAdapter.setFilms(films);
                }
            }
        });

        mViewModel.getLoadingIndicator().observe(this, new Observer<Boolean>() {
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
        loadingProgressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(this, ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemClick(int position) {
        Intent filmActivityIntent = new Intent(this, FilmActivity.class);
        filmActivityIntent.putExtra(FILMS_TAG, "Film Id: " + mViewModel.getFilms().getValue().get(position).getId());
        startActivity(filmActivityIntent);
    }
}