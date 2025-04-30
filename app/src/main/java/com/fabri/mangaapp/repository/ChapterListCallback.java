package com.fabri.mangaapp.repository; // Asegúrate de que sea tu paquete repository

import com.fabri.mangaapp.model.Chapter; // Importa tu modelo Chapter
import java.util.List; // Importa List

// Interfaz para el callback de la obtención de la lista de capítulos
public interface ChapterListCallback {
    void onSuccess(List<Chapter> chapterList); // Se llama cuando la lista de capítulos es obtenida exitosamente
    void onError(Throwable t); // Se llama si ocurre un error
}