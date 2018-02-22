package com.udacity.popularmovies1.popularmoviesstage2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by federico.creti on 22/02/2018.
 */

public class ApiVideosModel {
    @SerializedName("id")
    private long id;

    @SerializedName("results")
    private List<Video> results;

    public void setId(long id) { this.id = id; }
    public long getId() { return id; }

    public void setResults(List<Video> results) { this.results = results; }
    public List<Video> getResults() { return results; }
}
