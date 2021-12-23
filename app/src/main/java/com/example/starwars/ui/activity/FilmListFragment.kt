package com.example.starwars.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.starwars.ConnectionUtils
import com.example.starwars.R
import com.example.starwars.databinding.FragmentFilmListBinding
import com.example.starwars.domain.model.Film
import com.example.starwars.ui.adapter.FilmsAdapter
import com.example.starwars.ui.adapter.FilmsAdapter.FilmClickListener
import com.example.starwars.ui.viewmodel.FilmListActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class FilmListFragment : Fragment(), FilmClickListener {

    private lateinit var binding: FragmentFilmListBinding
    private lateinit var mAdapter: FilmsAdapter
    val mViewModel: FilmListActivityViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        requireContext().theme.applyStyle(R.style.Theme_StarWars, true)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()

        binding = FragmentFilmListBinding.inflate(layoutInflater)

        mAdapter = FilmsAdapter(this)

        with(mViewModel) {
            init()
            loadData(ConnectionUtils.isInternetConnected(this@FilmListFragment.requireContext()))
            filmsLiveData.observe(viewLifecycleOwner, { films ->
                Log.d(TAG, "onCreate: observer got the response")
                if (films == null || films.isEmpty()) {
                    showErrorMessage()
                    currentPage = 0
                } else {
                    if (!ConnectionUtils.isInternetConnected(this@FilmListFragment.requireContext())) {
                        showCacheMessage()
                        currentPage = 0
                    }
                    Log.d(TAG, "onCreate: set given films")
                    mAdapter.setFilms(films as ArrayList<Film>)
                    currentPage += 1
                }
            })

            indicatorLiveData.observe(viewLifecycleOwner, {
                    isLoading -> binding.pbLoadingIndicator.isVisible = isLoading
            })
        }

        with(binding) {
            rvFilms.adapter = mAdapter
            rvFilms.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    Log.d(TAG, "onScrolled: ")
                    if (!recyclerView.canScrollVertically(1)) {
                        mViewModel.loadData(ConnectionUtils.isInternetConnected(this@FilmListFragment.requireContext()))
                    }
                }
            })
        }

        return binding.root
    }


    private fun showErrorMessage() {
        Toast.makeText(this.requireContext(), ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
    }

    private fun showCacheMessage() {
        Toast.makeText(this.requireContext(), CACHE_MESSAGE, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(position: Int) {
        Navigation.findNavController(this.requireView())
            .navigate(FilmListFragmentDirections.actionFilmListFragmentToFilmFragment(
                mViewModel.filmsLiveData.value!![position].id)
            )
    }


    companion object {
        const val FILM_ID_EXTRA_KEY = "films"
        const val TAG = "asd"
        const val ERROR_MESSAGE = "No result in cache. Please connect to the internet"
        const val CACHE_MESSAGE = "No internet connection. Showing cache"
    }
}