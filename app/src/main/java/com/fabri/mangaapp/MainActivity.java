// MainActivity.java
package com.fabri.mangaapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fabri.mangaapp.adaptador.AnimeAdapter;
import com.fabri.mangaapp.model.Anime;
import com.fabri.mangaapp.model.AnimeResponse;
import com.fabri.mangaapp.servicio.ApiClient;
import com.fabri.mangaapp.servicio.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AnimeAdapter adapter;
    private ProgressBar progressBar;
    private EditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        searchInput = findViewById(R.id.searchInput);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Búsqueda dinámica mientras el usuario escribe
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                searchAnime(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Realizamos búsqueda inicial de "Naruto"
        searchAnime("Naruto");
    }

    private void searchAnime(String query) {
        if (query.isEmpty()) {
            Toast.makeText(MainActivity.this, "Por favor, ingrese un término de búsqueda", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<AnimeResponse> call = apiService.searchAnime(query);

        call.enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(Call<AnimeResponse> call, Response<AnimeResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    // Si la respuesta es exitosa, maneja los datos
                    if (response.body() != null) {
                        List<Anime> animeList = response.body().data;
                        if (animeList != null && !animeList.isEmpty()) {
                            adapter = new AnimeAdapter(MainActivity.this, animeList);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(MainActivity.this, "No se encontraron resultados para: " + query, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // Si la respuesta no es exitosa, muestra el código de error y el mensaje
                    Toast.makeText(MainActivity.this, "Error en la respuesta de la API. Código: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Código de respuesta: " + response.code() + ", Mensaje: " + response.message());
                }
            }


            @Override
            public void onFailure(Call<AnimeResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}