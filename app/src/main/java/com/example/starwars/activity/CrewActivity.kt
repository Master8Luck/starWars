package com.example.starwars.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.starwars.R
import com.example.starwars.adapter.CrewAdapter
import com.example.starwars.databinding.ActivityCrewBinding
import com.example.starwars.viewmodel.FilmActivityViewModel
import java.util.*

class CrewActivity : AppCompatActivity() {
    private val mViewModel: FilmActivityViewModel by viewModels()
    private lateinit var mAdapter: CrewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCrewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getIntExtra(FilmListActivity.FILM_ID_EXTRA_KEY, 0)

        mViewModel.init(id)
        mViewModel.filmLiveData!!.observe(
            this,
            Observer { film ->
                if (film != null) {
                    mAdapter!!.setData(film.crews)
                }
            })
        mAdapter = CrewAdapter(ArrayList())
        binding.crewRv.adapter = mAdapter
    }
}