package com.example.saurav.maths;

public class Movie {
    private String title, data, tag;

    public Movie() {
    }

    public Movie(String title, String data, String tag) {
        this.title = title;
        this.data = data;
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String gettag() {
        return tag;
    }

    public void settag(String tag) {
        this.tag = tag;
    }

    public String getdata() {
        return data;
    }

    public void setdata(String data) {
        this.data = data;
    }
}