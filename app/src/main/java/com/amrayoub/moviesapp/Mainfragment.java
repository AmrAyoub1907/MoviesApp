package com.amrayoub.moviesapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.style.BackgroundColorSpan;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Mainfragment extends Fragment {
    GridView Maingridview;
    String all_movies;

    private NameListener mListener;
    void setNameListener(NameListener nameListener) {
        this.mListener = nameListener;
    }
    ArrayList<MovieInfo> movieInfo = new ArrayList<>();
    ArrayList<MovieInfo> favouritsMovies = new ArrayList<>();
    ArrayList<MovieInfo> mainList = new ArrayList<>();
    String Popular_Movies = "http://api.themoviedb.org/3/movie/popular?api_key=107ed75bf9e25ec06bfe9fd33d042579";
    String Top_rated_Movies = "http://api.themoviedb.org/3/movie/top_rated?api_key=107ed75bf9e25ec06bfe9fd33d042579";
    List<String> chooselist = new ArrayList<String>();
    RequestQueue requestQueue;
    GridViewAdapter gridViewAdapter;
    Boolean last=false;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v2 = inflater.inflate(R.layout.mainfragment,container,false);
        requestQueue = Volley.newRequestQueue(getContext());
        chooselist.add("Most Popular Movies");
        chooselist.add("Top Rated Movies");
        chooselist.add("Favourite Movies");
        Maingridview = (GridView) v2.findViewById(R.id.MaingridView);
        final FloatingActionButton fab = (FloatingActionButton) v2.findViewById(R.id.fab);
        if(last==false){
        SortHolder.setInput("Most Popular Movies");
        all_movies = Popular_Movies;
        sendRequest(all_movies);}
        else{
            if(SortHolder.getInput()=="Most Popular Movies"){
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Most Popular");
                sendRequest(Popular_Movies);
            }
            else if (SortHolder.getInput()=="Top Rated Movies"){
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Top Rated");
                sendRequest(Top_rated_Movies);
            }
            else if(SortHolder.getInput()=="Favourite Movies"){
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My Favourites");
                Maingridview.setAdapter(null);
                DatabaseHandler db=new DatabaseHandler(getActivity());
                favouritsMovies=db.getAllContacts();
                gridViewAdapter = new GridViewAdapter(getActivity(), R.layout.poster, favouritsMovies);
                Maingridview.setAdapter(gridViewAdapter);
                mainList=favouritsMovies;
            }
        }
        Maingridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieInfo name = mainList.get(position);
                mListener.setSelectedName(name);
            }
        });
        return v2;
    }
    public void ShowAlertDialogWithListview()
    {
        final int[] x = new int[1];
        final CharSequence[] sortlist = chooselist.toArray(new String[chooselist.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle("Sort");
        //Create sequence of items
        dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setItems(sortlist, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int item) {
                x[0] = item;
                if (x[0] == 0)
                {
                    SortHolder.setInput("Most Popular Movies");
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Most Popular");
                    sendRequest(Popular_Movies);

                }
                else if (x[0] == 1){
                    SortHolder.setInput("Top Rated Movies");
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Top Rated");
                    sendRequest(Top_rated_Movies);
                }
                else {//favourite
                    SortHolder.setInput("Favourite Movies");
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My Favourites");
                    Maingridview.setAdapter(null);
                    DatabaseHandler db=new DatabaseHandler(getActivity());
                    favouritsMovies=db.getAllContacts();
                    gridViewAdapter = new GridViewAdapter(getActivity(), R.layout.poster, favouritsMovies);
                    Maingridview.setAdapter(gridViewAdapter);
                    mainList=favouritsMovies;
                }
            }

        });
        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();
            }
    public void choose_sort(View view) {
        ShowAlertDialogWithListview();
    }

    private void sendRequest(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    movieInfo.clear();
                    JSONArray resultArray = response.getJSONArray("results");
                    for (int i = 0; i < resultArray.length(); i++) {
                        JSONObject theObject = resultArray.getJSONObject(i);
                        MovieInfo obj = new MovieInfo();
                        obj.setMovieImageUrl(theObject.getString("poster_path"));
                        obj.setMovieName(theObject.getString("original_title"));
                        obj.setMovieRate(theObject.getString("vote_average"));
                        obj.setMovieOverview(theObject.getString("overview"));
                        obj.setMovieReleaseDate(theObject.getString("release_date"));
                        obj.setMovieID(theObject.getString("id"));
                        movieInfo.add(obj);
                    }
                    gridViewAdapter = new GridViewAdapter(getActivity(), R.layout.poster, movieInfo);
                    Maingridview.setAdapter(gridViewAdapter);
                    mainList=movieInfo;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });
// Add the request to the RequestQueue.
        requestQueue.add(jsonObjectRequest);
    }
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        last=true;
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Maingridview.setNumColumns(3);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Maingridview.setNumColumns(2);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favourites:
                SortHolder.setInput("Favourite Movies");
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My Favourites");
                Maingridview.setAdapter(null);
                DatabaseHandler db=new DatabaseHandler(getActivity());
                favouritsMovies=db.getAllContacts();
                gridViewAdapter = new GridViewAdapter(getActivity(), R.layout.poster, favouritsMovies);
                Maingridview.setAdapter(gridViewAdapter);
                mainList=favouritsMovies;
                return true;

            case R.id.mostpopular:

                SortHolder.setInput("Most Popular Movies");
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Most Popular");
                sendRequest(Popular_Movies);
                return true;

            case R.id.toprated:
                SortHolder.setInput("Top Rated Movies");
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Top Rated");
                sendRequest(Top_rated_Movies);

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


}
