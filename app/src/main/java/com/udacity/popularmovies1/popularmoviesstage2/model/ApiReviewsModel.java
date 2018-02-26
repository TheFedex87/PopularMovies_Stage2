package com.udacity.popularmovies1.popularmoviesstage2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by federico.creti on 22/02/2018.
 */

public class ApiReviewsModel {
    @SerializedName("id")
    private long id;

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<Review> results;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }
}
