package com.example.starwars.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.starwars.ConnectionUtils
import com.example.starwars.databinding.FragmentCrewBinding
import com.example.starwars.ui.adapter.CrewAdapter
import com.example.starwars.ui.viewmodel.FilmActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CrewFragment : Fragment() {
    private val mViewModel: FilmActivityViewModel by viewModels()
    lateinit var mAdapter: CrewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentCrewBinding.inflate(layoutInflater)
        val id = CrewFragmentArgs.fromBundle(requireArguments()).id

        mAdapter = CrewAdapter()

        mViewModel.init(id)
        mViewModel.loadData(ConnectionUtils.isInternetConnected(this.requireContext()))
        mViewModel.filmLiveData.observe(
            viewLifecycleOwner,
            { film ->
                if (film != null) {
                    mAdapter.setData(film.crews)
                }
            })

        binding.crewRv.adapter = mAdapter

        return binding.root
    }
}