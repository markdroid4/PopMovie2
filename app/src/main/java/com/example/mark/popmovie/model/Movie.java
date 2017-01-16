package com.example.mark.popmovie.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 1/11/17.
 */

public class Movie implements Parcelable {

    private String title;
    private String rating;
    private String releaseDate;
    private String imagePath;
    private String overview;
    private final String IMAGE_PREFIX = "http://image.tmdb.org/t/p/";
    //private List<String> actors;


    /**
     * Size can be specified per TMDB settings:
     * "w92", "w154", "w185", "w342", "w500", "w780", or "original"
     * @param size
     * @return
     */
    public String getImgPrefix(String size) {
        return IMAGE_PREFIX + size;
    }

    public Movie(String title, String rating, String imagePath, String overview,
                 String releaseDate) {
        this.title = title;
        this.rating = rating;
        this.imagePath = imagePath;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setRating(String rating) {
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

    private Movie(Parcel in)
    {
        this.title = in.readString();
        this.rating = in.readString();
        this.imagePath = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        //this.actors = new ArrayList<>();
        //in.readTypedList(actors, );
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(rating);
        parcel.writeString(imagePath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
    }

    public final static Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>()
    {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[0];
        }
    };
}
