// model/Chapter.java
package com.fabri.mangaapp.model;

import com.google.gson.annotations.SerializedName;

public class Chapter {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("url") // Si la API directamente da una URL para leer el capítulo
    private String url;

    @SerializedName("number") // Si la API proporciona el número del capítulo
    private int number;

    // ... otros campos que la API pueda devolver ...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}