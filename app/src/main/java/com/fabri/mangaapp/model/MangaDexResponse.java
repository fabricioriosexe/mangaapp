package com.fabri.mangaapp.model;


import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MangaDexResponse<T> {
    @SerializedName("data") // Mapea el campo JSON "data"
    private List<T> data;
    private Integer total;
    private Integer limit;
    private Integer offset;
    private List<ApiError> errors;

    // Getters y Setters (Gson puede funcionar sin ellos si los campos son públicos,
    // pero los getters/setters son una mejor práctica de encapsulación)
    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public List<ApiError> getErrors() {
        return errors;
    }

    public void setErrors(List<ApiError> errors) {
        this.errors = errors;
    }
    /**
     * Codigo Hecho
     * Por
     * Fabricio Rios
     * fabriciorios0103@gmail.com
     * Git Hub
     * https://github.com/fabricioriosexe*/
}