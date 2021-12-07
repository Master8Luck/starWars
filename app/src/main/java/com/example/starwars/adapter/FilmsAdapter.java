package com.example.starwars.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.starwars.R;
import com.example.starwars.model.Film;
import com.example.starwars.retrofit.ImageAPI;
import com.example.starwars.retrofit.RetrofitImageClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmsAdapter extends RecyclerView.Adapter<FilmsAdapter.ViewHolder> {
    private List<Film> films;
    private LayoutInflater inflater;
    private FilmClickListener filmClickListener;
    private ImageAPI imageAPI;

    public FilmsAdapter(List<Film> films, Context context, FilmClickListener filmClickListener) {
        this.films = films;
        this.inflater = LayoutInflater.from(context);
        this.filmClickListener = filmClickListener;
        imageAPI = RetrofitImageClient.getInstance().create(ImageAPI.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.film_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Film film = films.get(position);
        holder.FilmTitleTextView.setText(film.getTitle());
        holder.FilmInfoTextView.setText("Average vote: " + film.getVoteAverage());
        imageAPI.getImage(film.getPosterPath())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        holder.FilmIconImageView.setImageBitmap(BitmapFactory.decodeStream(response.body().byteStream()));
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        holder.FilmIconImageView.setImageResource(R.drawable.ic_launcher_background);
                    }
                });

    }

    @Override
    public int getItemCount() {
        return films.size();
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

    public void setFilms(List<Film> filmList) {
        films.addAll(filmList);
        notifyDataSetChanged();
    }
}
