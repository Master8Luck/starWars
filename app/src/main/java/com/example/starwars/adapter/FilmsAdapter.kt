package com.example.starwars.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.starwars.R;
import com.example.starwars.StarWarsApp;
import com.example.starwars.model.Film;

import java.util.ArrayList;

public class FilmsAdapter extends RecyclerView.Adapter<FilmsAdapter.ViewHolder> {

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342/";

    private ArrayList<Film> mFilms;
    private LayoutInflater inflater;
    private FilmClickListener filmClickListener;

    public FilmsAdapter(ArrayList<Film> films, Context context, FilmClickListener filmClickListener) {
        this.mFilms = films;
        this.inflater = LayoutInflater.from(context);
        this.filmClickListener = filmClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.film_list_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Film film = mFilms.get(position);
        holder.FilmTitleTextView.setText(film.getTitle());
        holder.FilmInfoTextView.setText("Average vote: " + film.getVoteAverage());
        Glide.with(StarWarsApp.getContext())
                .load(IMAGE_BASE_URL + film.getPosterPath())
                .error(R.drawable.ic_launcher_background)
                .into(holder.FilmIconImageView);
    }

    @Override
    public int getItemCount() {
        return mFilms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView FilmTitleTextView;
        TextView FilmInfoTextView;
        ImageView FilmIconImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            FilmTitleTextView = itemView.findViewById(R.id.film_item_title);
            FilmInfoTextView = itemView.findViewById(R.id.film_item_info);
            FilmIconImageView = itemView.findViewById(R.id.film_item_icon);
        }

        @Override
        public void onClick(View v) {
            filmClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface FilmClickListener {
        void onItemClick(int position);
    }

    public void setFilms(ArrayList<Film> filmList) {
        mFilms = filmList;
        notifyDataSetChanged();
    }
}
