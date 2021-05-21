package com.example.moviebox;

public class Movies {
    String nameMovie;
    int imageMovie;

    public Movies(String nameMovie, int imageMovie) {
        this.nameMovie = nameMovie;
        this.imageMovie = imageMovie;
    }

    public String getNameMovie() {
        return nameMovie;
    }

    public int getImageMovie() {
        return imageMovie;
    }
}
