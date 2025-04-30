package com.fabri.mangaapp.model; // Asegúrate de que sea tu paquete model

import com.google.gson.annotations.SerializedName;
import java.util.List;

// Modelo para la respuesta de la API al obtener los detalles de un solo manga (GET /manga/{id})
public class SingleMangaApiResponse {
    private String result; // Normalmente "ok"
    private String response; // Normalmente "entity"
    @SerializedName("data") // Mapea el campo JSON "data"
    private Manga data; // <-- ¡Aquí es un solo objeto Manga, no una lista!
    private List<Relationship> relationships; // Las relaciones incluidas (portada, autor, etc.)
    // Puede que también haya un campo 'errors', pero no lo necesitamos para el éxito

    // Getters y Setters (puedes generarlos automáticamente)
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
    public Manga getData() { return data; } // <-- Devuelve un solo Manga
    public void setData(Manga data) { this.data = data; }
    public List<Relationship> getRelationships() { return relationships; }
    public void setRelationships(List<Relationship> relationships) { this.relationships = relationships; }
}
/**
 * Codigo Hecho
 * Por
 * Fabricio Rios
 * fabriciorios0103@gmail.com
 * Git Hub
 * https://github.com/fabricioriosexe*/