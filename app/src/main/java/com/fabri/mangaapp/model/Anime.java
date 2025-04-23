// model/Anime.java
package com.fabri.mangaapp.model;

import com.google.gson.annotations.SerializedName;

public class Anime {
    @SerializedName("title")
    public String title;

    @SerializedName("images")
    public Images images;

    @SerializedName("synopsis")
    public String synopsis;

    @SerializedName("mal_id") // Asegúrate de que coincida con el nombre en la API
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}