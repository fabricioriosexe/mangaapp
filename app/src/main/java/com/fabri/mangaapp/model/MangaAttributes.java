package com.fabri.mangaapp.model;

import java.util.Iterator;
import java.util.Map;

public class MangaAttributes {
    private Map<String, String> title; // El título en varios idiomas { "en": "...", "es": "..." }
    private Map<String, String> description; // La descripción en varios idiomas
    private String originalLanguage;
    // ... otros atributos relevantes

    // Getters y Setters
    public Map<String, String> getTitle() {
        return title;
    }

    public void setTitle(Map<String, String> title) {
        this.title = title;
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    // Helper para obtener el título (ej: en inglés o el primero disponible)
    public String getDisplayTitle() { // Usamos un nombre diferente para no colisionar con el getter
        if (title != null) {
            if (title.containsKey("en")) {
                return title.get("en");
            } else if (!title.isEmpty()) {
                return title.values().iterator().next(); // Tomar el primer valor disponible
            }
        }
        return "Título Desconocido";
    }


    // Helper para obtener la descripción (priorizando español, luego inglés, luego el primero disponible)
    public String getDisplayDescription() {
        if (description != null) {
            // 1. Intentar obtener la descripción en español
            if (description.containsKey("es")) {
                return description.get("es");
            }
            // 2. Si no está en español, intentar en inglés
            else if (description.containsKey("en")) {
                return description.get("en");
            }
            // 3. Si no está en español ni inglés, tomar el primer idioma disponible
            else if (!description.isEmpty()) {
                Iterator<String> iterator = description.values().iterator();
                if (iterator.hasNext()) {
                    return iterator.next();
                }
            }
        }
        return "Sin descripción"; // Mensaje por defecto si no hay descripciones
    }
}/**
 * Codigo Hecho
 * Por
 * Fabricio Rios
 * fabriciorios0103@gmail.com
 * Git Hub
 * https://github.com/fabricioriosexe*/