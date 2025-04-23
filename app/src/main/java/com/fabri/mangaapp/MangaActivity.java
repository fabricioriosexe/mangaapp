package com.fabri.mangaapp;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fabri.mangaapp.adaptador.ChapterAdapter;
import com.fabri.mangaapp.model.Chapter;
import com.fabri.mangaapp.model.ChapterResponse;
import com.fabri.mangaapp.servicio.ApiClient;
import com.fabri.mangaapp.servicio.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MangaActivity extends AppCompatActivity {

    private TextView mangaTitle;
    private ProgressBar progressBar;
    private RecyclerView chaptersRecyclerView;
    private ChapterAdapter chapterAdapter;
    private int animeId;
    private String animeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga);

        mangaTitle = findViewById(R.id.mangaTitle);
        progressBar = findViewById(R.id.progressBar);
        chaptersRecyclerView = findViewById(R.id.chaptersRecyclerView);
        chaptersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        animeTitle = intent.getStringExtra("animeTitle");
        // animeId = intent.getIntExtra("animeId", 0); // Comenta esta línea

        animeId = 21; // Hardcodea el ID de One Piece (reemplaza si es diferente)
        Log.d("MangaActivity", "Usando animeId hardcodeado para One Piece: " + animeId);

        mangaTitle.setText(animeTitle);

        loadMangaChapters(animeId);
    }

    private void loadMangaChapters(int animeId) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ChapterResponse> call = apiService.getAnimeChapters(animeId);

        call.enqueue(new Callback<ChapterResponse>() {
            @Override
            public void onResponse(Call<ChapterResponse> call, Response<ChapterResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<Chapter> chapterList = response.body().getData();
                    if (chapterList != null && !chapterList.isEmpty()) {
                        chapterAdapter = new ChapterAdapter(MangaActivity.this, chapterList);
                        chaptersRecyclerView.setAdapter(chapterAdapter);
                    } else {
                        Toast.makeText(MangaActivity.this, "No se encontraron capítulos para este manga.", Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 404) {
                    Toast.makeText(MangaActivity.this, "No se encontraron capítulos para este manga.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MangaActivity.this, "Error al cargar los capítulos. Código: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Error al cargar capítulos. Código: " + response.code() + ", Mensaje: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ChapterResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MangaActivity.this, "Error al conectar con la API: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("API_ERROR", "Error de conexión: " + t.getMessage());
            }
        });
    }
}