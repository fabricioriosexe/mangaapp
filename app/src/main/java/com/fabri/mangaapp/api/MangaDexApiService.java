package com.fabri.mangaapp.api;

import com.fabri.mangaapp.model.Chapter;
import com.fabri.mangaapp.model.Manga;
import com.fabri.mangaapp.model.MangaDexResponse;
import com.fabri.mangaapp.model.SingleMangaApiResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query; // Asegúrate de importar Query

public interface MangaDexApiService {

    // Endpoint para buscar mangas
    @GET("manga") // El path relativo al base URL
    Call<MangaDexResponse<Manga>> searchManga( // Retorna un Call
                                               @Query("title") String title, // Parámetro de consulta 'title'
                                               @Query("availableTranslatedLanguage[]") List<String> translatedLanguage, // Filtrar por idioma
                                               @Query("limit") Integer limit, // Límite de resultados
                                               @Query("offset") Integer offset, // Offset para paginación
                                               @Query("includes[]") List<String> includes // <-- ¡ESTA ES LA LÍNEA AÑADIDA!
    );

    // --- ¡NUEVA FUNCIÓN! Endpoint para obtener los detalles de un manga por su ID ---
    // URL: https://api.mangadex.org/manga/{id}
    // --- ¡FUNCIÓN MODIFICADA! Endpoint para obtener los detalles de un manga ---
    @GET("manga/{id}")
    Call<SingleMangaApiResponse> getMangaDetails( // <-- ¡Ahora devuelve SingleMangaApiResponse!
                                                  @Path("id") String mangaId,
                                                  @Query("includes[]") List<String> includes
    );
    // --- ¡NUEVA FUNCIÓN! Endpoint para obtener la lista de capítulos de un manga ---
    // URL: https://api.mangadex.org/chapter?manga[]={mangaId}&translatedLanguage[]={lang}
    @GET("chapter")
    Call<MangaDexResponse<Chapter>> getChapterList(
            @Query("manga") String mangaId, // <-- ¡Ahora usa @Query("manga") y acepta un solo String!
            @Query("translatedLanguage[]") List<String> translatedLanguage,
            @Query("order[volume]") String orderVolume,
            @Query("order[chapter]") String orderChapter,
            @Query("limit") Integer limit,
            @Query("offset") Integer offset,
            @Query("includes[]") List<String> includes
    );
    // (Más tarde añadirás funciones para otros endpoints retornando Call<...>)
}/**
 * Codigo Hecho
 * Por
 * Fabricio Rios
 * fabriciorios0103@gmail.com
 * Git Hub
 * https://github.com/fabricioriosexe*/