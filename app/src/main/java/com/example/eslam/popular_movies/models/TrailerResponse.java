package com.example.eslam.popular_movies.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by eslam on 10/09/2016.
 */

public class TrailerResponse
{
    @SerializedName("id")
    @Expose
    private int id;

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }
    @SerializedName("results")
    @Expose
    private ArrayList<Trailer> results;

    public ArrayList<Trailer> getResults() { return this.results; }

    public void setResults(ArrayList<Trailer> results) { this.results = results; }
}