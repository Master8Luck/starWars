package com.example.starwars.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.starwars.ConnectionUtils
import com.example.starwars.databinding.ActivityCrewBinding
import com.example.starwars.ui.adapter.CrewAdapter
import com.example.starwars.ui.viewmodel.FilmActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CrewActivity : AppCompatActivity() {
    private val mViewModel: FilmActivityViewModel by viewModels()
    lateinit var mAdapter: CrewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCrewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getIntExtra(FilmListActivity.FILM_ID_EXTRA_KEY, 0)

        mAdapter = CrewAdapter()

        mViewModel.init(id)
        mViewModel.loadData(ConnectionUtils.isInternetConnected(this))
        mViewModel.filmLiveData.observe(
            this,
            { film ->
                if (film != null) {
                    mAdapter.setData(film.crews)
                }
            })

        binding.crewRv.adapter = mAdapter
    }
}