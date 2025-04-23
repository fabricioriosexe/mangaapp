package com.fabri.mangaapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
public class AnimeResponse {
    @SerializedName("data")
    public List<Anime> data;
}
