package com.amrayoub.moviesapp;


/**
 * Created by Amr Ayoub on 01/11/2016.
 */
 class Holder {
    private static MovieInfo minput = null;
    public static void setInput(MovieInfo value) { minput = value; }
    public static MovieInfo getInput() { return minput; }
}
