package com.fabri.mangaapp.repository; // Asegúrate de que sea tu paquete repository

import com.fabri.mangaapp.model.Manga; // Importa tu modelo Manga

// Interfaz para el callback de la obtención de detalles de un solo manga
public interface MangaDetailsCallback {
    void onSuccess(Manga manga); // Se llama cuando los detalles del manga son obtenidos exitosamente
    void onError(Throwable t); // Se llama si ocurre un error
}