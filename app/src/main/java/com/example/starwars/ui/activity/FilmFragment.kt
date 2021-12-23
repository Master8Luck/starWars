package com.example.starwars.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.starwars.API_ENDPOINTS.IMAGE_BASE_URL
import com.example.starwars.ConnectionUtils
import com.example.starwars.R
import com.example.starwars.databinding.FragmentFilmBinding
import com.example.starwars.domain.model.Film
import com.example.starwars.domain.model.Genre
import com.example.starwars.ui.activity.FilmListFragment.Companion.TAG
import com.example.starwars.ui.adapter.CrewAdapter
import com.example.starwars.ui.viewmodel.FilmActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilmFragment : Fragment() {

    private val mViewModel: FilmActivityViewModel by viewModels()
    lateinit var mAdapter: CrewAdapter
    private val binding by lazy { FragmentFilmBinding.inflate(layoutInflater) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {

        val id = FilmFragmentArgs.fromBundle(requireArguments()).id
        mViewModel.init(id)
        mViewModel.loadData(ConnectionUtils.isInternetConnected(requireContext()))
        mViewModel.filmLiveData.observe(viewLifecycleOwner,
            { t -> setData(t) })

        mAdapter = CrewAdapter()
        binding.filmCrewRv.adapter = mAdapter

        binding.filmToFullCrew.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(FilmFragmentDirections.actionFilmFragmentToCrewFragment(id))
        }
        return binding.root
    }

    private fun setData(film: Film?) {
        if (film != null) {

            if (!ConnectionUtils.isInternetConnected(requireContext())) {
                Toast.makeText(this.requireContext(), FilmListFragment.CACHE_MESSAGE, Toast.LENGTH_SHORT).show()
            }
            Log.d(TAG, "setData: " + film.title)

            with(binding) {
                Glide.with(requireContext())
                    .load(IMAGE_BASE_URL + film.posterPath)
                    .error(R.drawable.ic_launcher_background)
                    .into(filmIcon)
                filmTitle.text = film.title
                filmDate.text = film.releaseDate
                filmGenres.text = Genre.convertToString(film.genres)
                filmOverview.text = film.overview
                mAdapter.setData(film.crews)

            }
        } else {
            mAdapter.setData(null)
            Toast.makeText(this.requireContext(), FilmListFragment.ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
        }
    }

}