package com.example.starwars.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.starwars.ConnectionUtils
import com.example.starwars.adapter.CrewAdapter
import com.example.starwars.databinding.ActivityCrewBinding
import com.example.starwars.viewmodel.FilmActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CrewActivity : AppCompatActivity() {
    private val mViewModel: FilmActivityViewModel by viewModels<FilmActivityViewModel>()
    @Inject lateinit var mAdapter: CrewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCrewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getIntExtra(FilmListActivity.FILM_ID_EXTRA_KEY, 0)

        mViewModel.init(id)
        mViewModel.loadData(ConnectionUtils.isInternetConnected(this))
        mViewModel.filmLiveData.observe(
            this,
            Observer { film ->
                if (film != null) {
                    mAdapter.setData(film.crews)
                }
            })

        binding.crewRv.adapter = mAdapter
    }
}