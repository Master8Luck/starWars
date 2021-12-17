package com.example.starwars.ui.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.starwars.API_ENDPOINTS.IMAGE_BASE_URL
import com.example.starwars.R
import com.example.starwars.StarWarsApp.Companion.context
import com.example.starwars.databinding.FilmListItemBinding
import com.example.starwars.domain.model.Film
import com.example.starwars.ui.activity.FilmListActivity.Companion.TAG

class FilmsAdapter(var filmClickListener: FilmClickListener) : RecyclerView.Adapter<FilmsAdapter.ViewHolder>() {
    private var mFilms: ArrayList<Film> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FilmListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val film = mFilms[position]
            binding.filmItemTitle.text = film.title
            binding.filmItemInfo.text = context?.getString(R.string.film_item_info) + film.voteAverage

            Glide.with(itemView.context)
                .load(IMAGE_BASE_URL + film.posterPath)
                .error(R.drawable.ic_launcher_background)
                .into(binding.filmItemIcon)

            itemView.setOnClickListener { filmClickListener.onItemClick(adapterPosition) }
        }
    }

    override fun getItemCount(): Int {
        return mFilms.size
    }

    inner class ViewHolder(val binding: FilmListItemBinding)
        : RecyclerView.ViewHolder(binding.root)

    interface FilmClickListener {
        fun onItemClick(position: Int)
    }

    fun setFilms(filmList: ArrayList<Film>) {
        mFilms = filmList
        Log.d(TAG, "setFilms: inside adapter")
        notifyDataSetChanged()
    }

}