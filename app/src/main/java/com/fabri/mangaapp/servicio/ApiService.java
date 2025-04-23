
package com.fabri.mangaapp.servicio;
// ApiService.java
import com.fabri.mangaapp.model.AnimeResponse;
import com.fabri.mangaapp.model.ChapterResponse; // Importa ChapterResponse
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("anime")
    Call<AnimeResponse> searchAnime(@Query("q") String query);

    @GET("anime/{id}/chapters") // Ajusta el endpoint según tu API
    Call<ChapterResponse> getAnimeChapters(@Path("id") int id);
}

