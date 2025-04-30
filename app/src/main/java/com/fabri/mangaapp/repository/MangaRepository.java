package com.fabri.mangaapp.repository;

// Importaciones necesarias
import android.util.Log; // Para logging

import com.fabri.mangaapp.api.MangaDexApiService;
import com.fabri.mangaapp.api.RetrofitClient;
import com.fabri.mangaapp.model.Manga;
import com.fabri.mangaapp.model.Chapter;
import com.fabri.mangaapp.model.MangaDexResponse;
import com.fabri.mangaapp.model.SingleMangaApiResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MangaRepository {

    private static final String TAG = "MangaRepository";

    private final MangaDexApiService apiService;

    public MangaRepository() {
        apiService = RetrofitClient.getApiService();
    }

    // --- Método para buscar mangas por título ---
    public void searchManga(String query, MangaSearchCallback callback) {
        Log.d(TAG, "Searching manga with query: " + query);
        Call<MangaDexResponse<Manga>> call = apiService.searchManga(
                query,
                Collections.singletonList("es"), // Filtrar por español en la búsqueda
                null, // Límite por defecto de la API
                null,  // Offset por defecto
                Arrays.asList("cover_art") // Solicitar cover_art en la búsqueda para la lista
        );

        call.enqueue(new Callback<MangaDexResponse<Manga>>() {
            @Override
            public void onResponse(Call<MangaDexResponse<Manga>> call, Response<MangaDexResponse<Manga>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Manga> mangaList = response.body().getData();
                    Log.d(TAG, "Search successful. Found " + (mangaList != null ? mangaList.size() : 0) + " mangas.");
                    callback.onSuccess(mangaList != null ? mangaList : Collections.emptyList());
                } else {
                    Log.e(TAG, "Error HTTP searching manga: " + response.code());
                    callback.onError(new Exception("Error HTTP searching manga: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<MangaDexResponse<Manga>> call, Throwable t) {
                Log.e(TAG, "Failure searching manga", t);
                callback.onError(t);
            }
        });
    }

    // --- Método para obtener los detalles completos de un manga por su ID ---
    public void getMangaDetails(String mangaId, MangaDetailsCallback callback) {
        Log.d(TAG, "Getting details for manga ID: " + mangaId);
        // Usamos SingleMangaApiResponse porque el endpoint /manga/{id} devuelve un solo objeto en 'data'
        Call<SingleMangaApiResponse> call = apiService.getMangaDetails(
                mangaId, // El ID del manga
                // Pedir relaciones importantes para la pantalla de detalles
                Arrays.asList("cover_art", "author", "artist", "tag")
        );

        call.enqueue(new Callback<SingleMangaApiResponse>() {
            @Override
            public void onResponse(Call<SingleMangaApiResponse> call, Response<SingleMangaApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    Log.d(TAG, "Manga details obtained successfully for ID: " + mangaId);
                    // Acceder directamente al campo 'data' que es un solo objeto Manga
                    callback.onSuccess(response.body().getData());
                } else {
                    Log.e(TAG, "Error HTTP getting manga details: " + response.code() + " for ID: " + mangaId);
                    callback.onError(new Exception("Error HTTP getting manga details: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<SingleMangaApiResponse> call, Throwable t) {
                Log.e(TAG, "Failure getting manga details for ID: " + mangaId, t);
                callback.onError(t);
            }
        });
    }


    // --- MÉTODO CORREGIDO FINAL 2.0! Usa el parámetro 'manga' (String) ---
    // --- MÉTODO getChapterList en MangaRepository.java (Sin filtro de idioma en la app) ---
    // Obtiene todos los capítulos para un manga y los pasa al callback.
    public void getChapterList(String mangaId, ChapterListCallback callback) {
        Log.d(TAG, "Getting all chapter list for manga ID (as 'manga' param): " + mangaId);
        // Llamada a la función del servicio API que ahora funciona: GET /chapter?manga={id}
        Call<MangaDexResponse<Chapter>> call = apiService.getChapterList(
                mangaId, // <-- 1er argumento: ID del manga (String)
                null, // <-- 2do argumento: translatedLanguage[] (null) - Ya no filtramos aquí ni en la app por idioma
                null, // <-- 3er argumento: order[volume] (null)
                null, // <-- 4to argumento: order[chapter] (null)
                null, // <-- 5to argumento: limit (null) - La API devuelve un límite por defecto (creo que 10 o 100)
                null,  // <-- 6to argumento: offset (null)
                // Opcional: Si quieres los nombres del grupo de escaneo, intenta añadir de nuevo 'scanlation_group'.
                // PERO TEN CUIDADO: Añadir otros parámetros causó el error 400 antes. Podría volver a ocurrir.
                // Por ahora, para obtener *todos* los capítulos, lo dejamos simple.
                null // <-- 7mo argumento: includes[] (null)
        );

        // --- Este bloque ejecuta la llamada y pasa la lista COMPLETA al callback ---
        call.enqueue(new Callback<MangaDexResponse<Chapter>>() {
            @Override
            public void onResponse(Call<MangaDexResponse<Chapter>> call, Response<MangaDexResponse<Chapter>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Chapter> chapterList = response.body().getData();
                    Log.d(TAG, "Chapter list obtained successfully. Total count: " + (chapterList != null ? chapterList.size() : 0));

                    // --- ¡ELIMINAR EL BLOQUE DE FILTRADO POR IDIOMA! ---
                    // Antes teníamos:
                    // List<Chapter> spanishChapters = new ArrayList<>();
                    // for (Chapter chapter : chapterList) { ... añadir a spanishChapters ... }
                    // callback.onSuccess(spanishChapters);
                    // ----------------------------------------------------

                    // --- ¡Ahora pasamos la lista COMPLETA! ---
                    callback.onSuccess(chapterList != null ? chapterList : Collections.emptyList());
                    // ------------------------------------------

                } else {
                    Log.e(TAG, "Error HTTP getting chapter list: " + response.code() + " for ID: " + mangaId);
                    callback.onError(new Exception("Error HTTP getting chapter list: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<MangaDexResponse<Chapter>> call, Throwable t) {
                Log.e(TAG, "Failure getting chapter list for ID: " + mangaId, t);
                callback.onError(t);
            }
        });
        // --- Fin del bloque enqueue ---

        // --- Fin del bloque enqueue ---
    }
}