package com.fabri.mangaapp.model; // Asegúrate de que sea el mismo paquete que tus otros modelos

import com.google.gson.annotations.SerializedName;
import java.util.Map;
import java.util.List;

public class Chapter {
    private String id; // UUID del capítulo
    private String type; // "chapter"
    private ChapterAttributes attributes;
    private List<Relationship> relationships; // Para el grupo de escaneo

    // Getters y Setters (puedes generarlos automáticamente en Android Studio)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public ChapterAttributes getAttributes() { return attributes; }
    public void setAttributes(ChapterAttributes attributes) { this.attributes = attributes; }
    public List<Relationship> getRelationships() { return relationships; }
    public void setRelationships(List<Relationship> relationships) { this.relationships = relationships; }


    // Helper para obtener el nombre del grupo de escaneo
    public String getScanlationGroupName() {
        if (relationships != null) {
            for (Relationship rel : relationships) {
                if ("scanlation_group".equals(rel.getType())) {
                    // La API incluye el nombre del grupo en los atributos de la relación si se incluyó
                    if (rel.getAttributes() != null && rel.getAttributes().containsKey("name")) {
                        Object nameObj = rel.getAttributes().get("name");
                        if (nameObj instanceof String) {
                            return (String) nameObj;
                        }
                    }
                }
            }
        }
        return "Desconocido"; // Si no se encuentra el grupo
    }
}