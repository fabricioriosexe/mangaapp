// model/ChapterResponse.java
package com.fabri.mangaapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ChapterResponse {
    @SerializedName("data")
    private List<Chapter> data;

    @SerializedName("pagination") // Si la API tiene información de paginación
    private Pagination pagination;

    public List<Chapter> getData() {
        return data;
    }

    public void setData(List<Chapter> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}