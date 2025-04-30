package com.fabri.mangaapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Manga {
    private String id; // El UUID del manga
    private String type; // "manga"
    private MangaAttributes attributes;
    private List<Relationship> relationships; // Para obtener la portada, autor, etc.

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

    public MangaAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(MangaAttributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manga manga = (Manga) o;
        return Objects.equals(id, manga.id) && Objects.equals(type, manga.type) && Objects.equals(attributes, manga.attributes) && Objects.equals(relationships, manga.relationships);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, attributes, relationships);
    }

    public List<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<Relationship> relationships) {
        this.relationships = relationships;
    }
}/**
 * Codigo Hecho
 * Por
 * Fabricio Rios
 * fabriciorios0103@gmail.com
 * Git Hub
 * https://github.com/fabricioriosexe*/