package com.example.mark.popmovie.model;

import android.os.Parcelable;

/**
 * Created by mark on 7/22/17.
 */

public class MovieReview /*implements Parcelable */{

    private int id;
    private String author;
    private String content;
    private String url;

    public MovieReview(int id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //@Override
    //public int describeContents() {
    //    return 0;
    //}
}
