package com.fabri.mangaapp.model;

import java.util.Map;

public class Relationship {
    private String id; // El UUID del objeto relacionado
    private String type; // "cover_art", "author", "artist", etc.
    private Map<String, Object> attributes; // Atributos del objeto relacionado (ej: el nombre del archivo de la portada)

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    // Helper para encontrar el atributo 'fileName' si es una relación de 'cover_art'
    public String getCoverFileName() {
        if ("cover_art".equals(type) && attributes != null && attributes.containsKey("fileName")) {
            Object fileNameObj = attributes.get("fileName");
            if (fileNameObj instanceof String) {
                return (String) fileNameObj;
            }
        }
        return null;
    }
}
/**
 * Codigo Hecho
 * Por
 * Fabricio Rios
 * fabriciorios0103@gmail.com
 * Git Hub
 * https://github.com/fabricioriosexe*/