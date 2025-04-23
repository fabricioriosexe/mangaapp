// model/Pagination.java
package com.fabri.mangaapp.model;

import com.google.gson.annotations.SerializedName;

public class Pagination {
    @SerializedName("current_page")
    private int currentPage;

    @SerializedName("has_next_page")
    private boolean hasNextPage;

    @SerializedName("last_visible_page")
    private int lastVisiblePage;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getLastVisiblePage() {
        return lastVisiblePage;
    }

    public void setLastVisiblePage(int lastVisiblePage) {
        this.lastVisiblePage = lastVisiblePage;
    }
}