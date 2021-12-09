package com.example.starwars.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.starwars.R;
import com.example.starwars.adapter.CrewAdapter;
import com.example.starwars.model.Film;
import com.example.starwars.viewmodel.FilmActivityViewModel;

import java.util.ArrayList;

public class CrewActivity extends AppCompatActivity {

    private FilmActivityViewModel mViewModel;

    private CrewAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crew);

        int id = getIntent().getIntExtra(FilmListActivity.FILM_ID_EXTRA_KEY, 0);
        mViewModel = ViewModelProviders.of(this).get(FilmActivityViewModel.class);
        mViewModel.init(id);
        mViewModel.getFilmLiveData().observe(this, new Observer<Film>() {
            @Override
            public void onChanged(Film film) {
                mAdapter.setData(film.getCrews());
            }
        });


        mRecyclerView = findViewById(R.id.crew_rv);
        mAdapter = new CrewAdapter(new ArrayList<>(), this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);



    }
}