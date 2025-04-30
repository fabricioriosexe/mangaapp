package com.fabri.mangaapp.model; // Asegúrate de que sea el mismo paquete

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class ChapterAttributes {
    private String volume; // Número de volumen
    private String chapter; // Número de capítulo
    private String title; // Título del capítulo (si lo tiene)
    private String translatedLanguage; // Idioma de la traducción
    private String publishAt; // Fecha de publicación
    // ... otros atributos relevantes

    // Getters y Setters
    public String getVolume() { return volume; }
    public void setVolume(String volume) { this.volume = volume; }
    public String getChapter() { return chapter; }
    public void setChapter(String chapter) { this.chapter = chapter; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getTranslatedLanguage() { return translatedLanguage; }
    public void setTranslatedLanguage(String translatedLanguage) { this.translatedLanguage = translatedLanguage; }
    public String getPublishAt() { return publishAt; }
    public void setPublishAt(String publishAt) { this.publishAt = publishAt; }


    // Helper para obtener el título o número para mostrar
    public String getDisplayTitleOrNumber() {
        if (title != null && !title.trim().isEmpty()) {
            // Si tiene título, mostrar volumen + capítulo + título
            return (volume != null ? "Vol. " + volume + " " : "") +
                    (chapter != null ? "Cap. " + chapter + " " : "") +
                    title;
        } else if (chapter != null) {
            // Si no tiene título, mostrar volumen + capítulo
            return (volume != null ? "Vol. " + volume + " " : "") +
                    "Cap. " + chapter;
        } else if (volume != null) {
            // Si solo tiene volumen
            return "Vol. " + volume;
        }
        return "Capítulo Desconocido";
    }
}/**
 * Codigo Hecho
 * Por
 * Fabricio Rios
 * fabriciorios0103@gmail.com
 * Git Hub
 * https://github.com/fabricioriosexe*/