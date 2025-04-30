package com.fabri.mangaapp.repository;

import com.fabri.mangaapp.model.Manga;

import java.util.List;

// Interfaz para el callback del repositorio
public interface MangaSearchCallback {
    void onSuccess(List<Manga> mangaList); // Se llama cuando la búsqueda es exitosa
    void onError(Throwable t); // Se llama si ocurre un error (red, API, etc.)
}