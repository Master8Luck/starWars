package com.example.starwars.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.starwars.R
import com.example.starwars.adapter.CrewAdapter
import com.example.starwars.viewmodel.FilmActivityViewModel
import java.util.*

class CrewActivity : AppCompatActivity() {
    private var mViewModel: FilmActivityViewModel? = null
    private var mAdapter: CrewAdapter? = null
    private lateinit var mRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crew)
        val id = intent.getIntExtra(FilmListActivity.FILM_ID_EXTRA_KEY, 0)
        mViewModel = ViewModelProviders.of(this).get(FilmActivityViewModel::class.java)
        mViewModel!!.init(id)
        mViewModel!!.filmLiveData!!.observe(
            this,
            Observer { film ->
                if (film != null) {
                    mAdapter!!.setData(film.crews)
                }
            })
        mRecyclerView = findViewById(R.id.crew_rv)
        mAdapter = CrewAdapter(ArrayList(), this)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter
    }
}