package com.example.starwars.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.starwars.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?):
            View {
//        requireContext().theme.applyStyle(R.style.Theme_SplashTheme, true)
        val binding = FragmentSplashBinding.inflate(inflater)

        Handler(Looper.getMainLooper()).postDelayed({
            Navigation.findNavController(this.requireView()).navigate(SplashFragmentDirections.actionSplashFragmentToFilmListFragment())
        }, 2400)

        return binding.root
    }

}