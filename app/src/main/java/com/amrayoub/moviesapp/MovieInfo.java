package com.amrayoub.moviesapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.security.PublicKey;

/**
 * Created by Amr Ayoub on 21/10/2016.
 */

public class MovieInfo implements Serializable{
    private String MovieImageUrl="http://image.tmdb.org/t/p/w185/";
    private String MovieName;
    private String MovieID;
    private String MovieRate;
    private String MovieReleaseDate;
    private String MovieOverview;
    private String MovieTrailer;
    private String MovieReviews;
    public MovieInfo(){
        return;
    }
    public MovieInfo( String MovieID, String MovieName, String MovieRate, String MovieReleaseDate, String MovieOverview, String MovieReviews,String MovieImageUrl){
        this.MovieID=MovieID;
        this.MovieName=MovieName;
        this.MovieRate=MovieRate;
        this.MovieReleaseDate=MovieReleaseDate;
        this.MovieOverview=MovieOverview;
        this.MovieReviews=MovieReviews;
        this.MovieImageUrl=MovieImageUrl;
    }
    public String getMovieRate() {
        return MovieRate;
    }

    public void setMovieRate(String movieRate) {
        MovieRate = movieRate;
    }

    public String getMovieName() {
        return MovieName;
    }

    public void setMovieName(String movieName) {
        MovieName = movieName;
    }

    public String getMovieImageUrl() {
        return MovieImageUrl;
    }

    public void setMovieImageUrl(String movieImageUrl) {
        MovieImageUrl += movieImageUrl;
    }

    public String getMovieReleaseDate() {
        return MovieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        MovieReleaseDate = movieReleaseDate;
    }

    public String getMovieOverview() {
        return MovieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        MovieOverview = movieOverview;
    }


    public String getMovieTrailer() {
        return MovieTrailer;
    }

    public void setMovieTrailer(String movieTrailer) {
        MovieTrailer = movieTrailer;
    }

    public String getMovieReviews() {
        return MovieReviews;
    }

    public void setMovieReviews(String movieReviewsAuthor) {
        MovieReviews = movieReviewsAuthor;
    }

    public String getMovieID() {
        return MovieID;
    }

    public void setMovieID(String movieID) {
        MovieID = movieID;
    }


}
