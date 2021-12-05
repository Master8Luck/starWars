package com.example.starwars;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.starwars.model.Film;
import com.example.starwars.model.Films;
import com.example.starwars.retrofit.RetrofitClient;
import com.example.starwars.retrofit.StarAPI;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends Activity implements FilmsAdapter.FilmClickListener{

    public static final String FILMS_TAG = "films";
    public static final String API_KEY = "430dc4bcb90f3bd8e2616b75a712749c";
    public static final String TAG = "asd";
    int maxPage = 1;
    int page = 1;
    StarAPI starAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    FilmsAdapter adapter;
    RecyclerView recyclerView;
    ProgressBar loadingProgressBar;
    TextView errorTextView;
    List<Film> filmList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_StarWars);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = RetrofitClient.getInstance();
        starAPI = retrofit.create(StarAPI.class);

        recyclerView = findViewById(R.id.rv_films);
        errorTextView = findViewById(R.id.tv_error_message);
        loadingProgressBar = findViewById(R.id.pb_loading_indicator);
        adapter = new FilmsAdapter(filmList, this, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (filmList.size() == 0)
                    fetchData();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    if (page < maxPage) {
                        page++;
                        fetchData();
                    }
                }
            }
        });


        fetchData();
    }

    private void fetchData() {
        showLoadingIndicator();
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
            compositeDisposable.add(starAPI.getBaseFilms(API_KEY, page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Films>() {
                        @Override
                        public void accept(Films films) throws Throwable {
                            displayData(films);
                            maxPage = films.getPages();
                        }
                    }));
        } else {
            showErrorMessage();
        }
    }

    private void displayData(Films films) {
        errorTextView.setVisibility(View.INVISIBLE);
        loadingProgressBar.setVisibility(View.INVISIBLE);
        filmList.addAll(films.getFilms());
        adapter.notifyDataSetChanged();
    }

    private void showErrorMessage(){
        errorTextView.setVisibility(View.VISIBLE);
        loadingProgressBar.setVisibility(View.INVISIBLE);
    }

    private void showLoadingIndicator(){
        errorTextView.setVisibility(View.INVISIBLE);
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    public void onItemClick(int position) {
        Intent filmActivityIntent = new Intent(this, FilmActivity.class);
        filmActivityIntent.putExtra(FILMS_TAG, "Film Id: " + filmList.get(position).getId());
        startActivity(filmActivityIntent);
    }
}