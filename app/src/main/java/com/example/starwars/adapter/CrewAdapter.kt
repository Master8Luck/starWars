package com.example.starwars.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.starwars.API_ENDPOINTS.IMAGE_CREW_BASE_URL
import com.example.starwars.R
import com.example.starwars.StarWarsApp
import com.example.starwars.databinding.CrewListItemBinding
import com.example.starwars.model.Crew
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CrewAdapter @Inject constructor(): RecyclerView.Adapter<CrewAdapter.ViewHolder>() {

    private var mCrews: List<Crew> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CrewListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            val crew = mCrews[position]
            crewItemHeroTv.text = crew.name
            crewItemPersonTv.text = crew.country
            StarWarsApp.context?.let {
                Glide.with(it)
                    .load(IMAGE_CREW_BASE_URL + crew.logoPath)
                    .error(R.drawable.ic_launcher_background)
                    .override(128, 128)
                    .into(crewItemIv)
            }
        }
    }

    override fun getItemCount(): Int {
        return mCrews.size
    }

    inner class ViewHolder(val binding: CrewListItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun setData(data: List<Crew>?) {
        if (data != null) mCrews = data
        else mCrews = listOf()
        notifyDataSetChanged()
    }

}