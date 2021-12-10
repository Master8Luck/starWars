package com.example.starwars.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.starwars.R;
import com.example.starwars.StarWarsApp;
import com.example.starwars.model.Crew;

import java.util.List;

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.ViewHolder> {
    private static final String IMAGE_CREW_BASE_URL = "https://image.tmdb.org/t/p/w300/";

    private List<Crew> mCrews;
    private LayoutInflater mInflater;

    public CrewAdapter(List<Crew> crews, Context context) {
        mCrews = crews;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.crew_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Crew crew = mCrews.get(position);
        holder.mHeroNameTextView.setText(crew.getName());
        holder.mPersonNameTextView.setText(crew.getCountry());
        Glide.with(StarWarsApp.getContext())
                .load(IMAGE_CREW_BASE_URL + crew.getLogoPath())
                .error(R.drawable.ic_launcher_background)
                .override(128, 128)
                .into(holder.mPersonPhotoImageView);
    }

    @Override
    public int getItemCount() {
        if (mCrews == null)
            return 0;
        return mCrews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mPersonPhotoImageView;
        TextView mHeroNameTextView;
        TextView mPersonNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mPersonPhotoImageView = itemView.findViewById(R.id.crew_item_iv);
            mHeroNameTextView = itemView.findViewById(R.id.crew_item_hero_tv);
            mPersonNameTextView = itemView.findViewById(R.id.crew_item_person_tv);
        }
    }

    public void setData(List<Crew> data) {
        if (data != null)
            mCrews.addAll(data);
    }
}
