package com.fabri.mangaapp.adaptador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import androidx.recyclerview.widget.RecyclerView;

import com.fabri.mangaapp.MangaActivity;
import com.fabri.mangaapp.R;
import com.fabri.mangaapp.model.Anime;

import java.util.List;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {
    private Context context;
    private List<Anime> animeList;

    public AnimeAdapter(Context context, List<Anime> animeList) {
        this.context = context;
        this.animeList = animeList;
    }

    @Override
    public AnimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_anime, parent, false);
        return new AnimeViewHolder(view);
    }
    @Override
    public void onBindViewHolder(AnimeViewHolder holder, int position) {
        Anime anime = animeList.get(position);
        holder.titleTextView.setText(anime.getTitle());

        // Cargar la imagen usando Glide DESDE LA URL DE LA API
        if (anime.getImages() != null && anime.getImages().jpg != null && anime.getImages().jpg.image_url != null) {
            Glide.with(context)
                    .load(anime.getImages().jpg.image_url) // Esta es la URL que viene de la API
                    .into(holder.animeImageView);
        } else {
            // Muestra el placeholder si la URL no está disponible
            holder.animeImageView.setImageResource(R.drawable.ic_placeholder);
        }

        // ... (resto del código) ...
    }
    @Override
    public int getItemCount() {
        return animeList.size();
    }
    // adaptador/AnimeAdapter.java
    public static class AnimeViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView animeImageView; // Agrega esta línea

        public AnimeViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.animeTitle);
            animeImageView = itemView.findViewById(R.id.animeImage); // Inicializa el ImageView
        }
    }
}
