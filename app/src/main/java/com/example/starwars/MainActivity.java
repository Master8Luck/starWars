package com.example.starwars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
    public static final String TAG = "asd";
    StarAPI starAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    FilmsAdapter adapter;
    RecyclerView recyclerView;
    List<Film> filmList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.Theme_StarWars);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = RetrofitClient.getInstance();
        starAPI = retrofit.create(StarAPI.class);

        recyclerView = findViewById(R.id.rv_films);
        filmList.add(new Film("qwe", 2, "qwes", "gger"));
        adapter = new FilmsAdapter(filmList, this, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);


        fetchData();
    }

    private void fetchData() {
        compositeDisposable.add(starAPI.getFilms()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Films>() {
            @Override
            public void accept(Films films) throws Throwable {
                Log.d(TAG, "accept: " + films.getFilms().get(0).toString());
                displayData(films);
            }
        }));
    }

    private void displayData(Films films) {
        filmList.clear();
        filmList.addAll(films.getFilms());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    public void onItemClick(int position) {
        Intent filmActivityIntent = new Intent(this, FilmActivity.class);
        filmActivityIntent.putExtra(FILMS_TAG, filmList.get(position).getFullTitle());
        startActivity(filmActivityIntent);
    }
}