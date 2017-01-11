package com.example.mark.popmovie.model;

import static android.R.attr.rating;

/**
 * Created by mark on 1/11/17.
 */

public class Movie {

    private String title;
    private int rating;
    private String imagePath;
    private String overview;

    public Movie(String title, int rating, String imagePath, String overview) {
        this.title = title;
        this.rating = rating;
        this.imagePath = imagePath;
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
