package com.example.eslam.popular_movies.models;

import java.util.ArrayList;

/**
 * Created by eslam on 10/09/2016.
 */

public class ReviewResponse
{
    private int id;

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    private int page;

    public int getPage() { return this.page; }

    public void setPage(int page) { this.page = page; }

    private ArrayList<Review> results;

    public ArrayList<Review> getResults() { return this.results; }

    public void setResults(ArrayList<Review> results) { this.results = results; }

    private int total_pages;

    public int getTotalPages() { return this.total_pages; }

    public void setTotalPages(int total_pages) { this.total_pages = total_pages; }

    private int total_results;

    public int getTotalResults() { return this.total_results; }

    public void setTotalResults(int total_results) { this.total_results = total_results; }
}
