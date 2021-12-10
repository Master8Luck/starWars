package com.example.starwars.adapter

import android.content.Context
import com.example.starwars.model.Crew
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.starwars.R
import com.bumptech.glide.Glide
import com.example.starwars.StarWarsApp
import com.example.starwars.adapter.CrewAdapter
import android.widget.TextView

class CrewAdapter(private val mCrews: MutableList<Crew>?, context: Context?) :
    RecyclerView.Adapter<CrewAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.crew_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val crew = mCrews!![position]
        holder.mHeroNameTextView.text = crew.name
        holder.mPersonNameTextView.text = crew.country
        StarWarsApp.context?.let {
            Glide.with(it)
                .load(IMAGE_CREW_BASE_URL + crew.logoPath)
                .error(R.drawable.ic_launcher_background)
                .override(128, 128)
                .into(holder.mPersonPhotoImageView)
        }
    }

    override fun getItemCount(): Int {
        return mCrews?.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mPersonPhotoImageView: ImageView = itemView.findViewById(R.id.crew_item_iv)
        var mHeroNameTextView: TextView = itemView.findViewById(R.id.crew_item_hero_tv)
        var mPersonNameTextView: TextView = itemView.findViewById(R.id.crew_item_person_tv)

    }

    fun setData(data: List<Crew>?) {
        if (data != null) mCrews!!.addAll(data)
    }

    companion object {
        private const val IMAGE_CREW_BASE_URL = "https://image.tmdb.org/t/p/w300/"
    }

    init {
        mInflater = LayoutInflater.from(context)
    }
}