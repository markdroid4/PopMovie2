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
    private float rating;
    private String imagePath;
    private String overview;
    //private List<String> actors;


    public Movie(String title, float rating, String imagePath, String overview) {
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
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
        this.rating = in.readFloat();
        this.imagePath = in.readString();
        this.overview = in.readString();
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
        parcel.writeFloat(rating);
        parcel.writeString(imagePath);
        parcel.writeString(overview);

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
