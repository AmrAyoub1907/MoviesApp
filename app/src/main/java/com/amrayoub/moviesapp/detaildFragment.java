package com.amrayoub.moviesapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class detaildFragment extends Fragment {
    TextView MovieDate;
    String moviedate;
    TextView MovieRate;
    TextView MovieOveriew;
    TextView MovieAuthor;
    TextView MovieContent;
    ImageView MoviePoster;
    ListView MovieVideos;
    List<String>trailerlist=new ArrayList<>();
    Button button;
    RequestQueue requestQueue2;
    RequestQueue requestQueue3;
    DatabaseHandler db;
    MovieInfo detailonbj;
    String reviews="";
    String newline = System.getProperty("line.separator");
    boolean here=false;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(getActivity());
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detaild, container, false);
        ScrollView scrollView= (ScrollView) v.findViewById(R.id.scrollView);
        scrollView.requestFocus();
        requestQueue2= Volley.newRequestQueue(getContext());
        requestQueue3= Volley.newRequestQueue(getContext());
        MovieDate = (TextView) v.findViewById(R.id.movieDate);
        MovieOveriew = (TextView) v.findViewById(R.id.movieOverview);
        MovieRate = (TextView) v.findViewById(R.id.movieRate);
        MoviePoster = (ImageView) v.findViewById(R.id.moviePoster);
        MovieAuthor= (TextView) v.findViewById(R.id.movieauthor);
        MovieVideos= (ListView) v.findViewById(R.id.movievideos);
        button = (Button) v.findViewById(R.id.favouritebutton);

        detailonbj=Holder.getInput();

        moviedate = detailonbj.getMovieReleaseDate();//2016-12-1
        MovieDate.setText(moviedate.substring(0,4));//2016
        MovieRate.setText(detailonbj.getMovieRate().substring(0,1)+" / 10");//7.48-->7
        MovieOveriew.setText(detailonbj.getMovieOverview());
        MovieAuthor.setText(detailonbj.getMovieReviews());
        Picasso.with(getContext()).load(detailonbj.getMovieImageUrl()).into(MoviePoster);

        if(SortHolder.getInput().equals("Favourite Movies")){
            button.setVisibility(View.GONE);
            MovieVideos.setVisibility(View.GONE);
            TextView vi= (TextView) v.findViewById(R.id.textViewvideos);
            vi.setVisibility(View.GONE);
        }
        else{
            String mainurl = "http://api.themoviedb.org/3/movie/";
            String temp=detailonbj.getMovieID()+"";
            String url =mainurl + temp + "/reviews" + "?api_key=107ed75bf9e25ec06bfe9fd33d042579";//set reviewes
            sendRequest2(url);//set Reviews
            url =mainurl + temp + "/videos" + "?api_key=107ed75bf9e25ec06bfe9fd33d042579";//set trailers
            sendRequest3(url);//set Trailers
                if(db.getContactsCount(detailonbj.getMovieID())==0 ){
                button.setText("Mark AS Favourite");
            }
            else {
                button.setText("Already Favourite");
            }
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.getContactsCount(detailonbj.getMovieID())==0)
                {
                    button.setText("Already Favourite");

                    //add movie to The DataBase

                    MovieInfo dbobj=new MovieInfo(detailonbj.getMovieID(),
                            detailonbj.getMovieName(),
                            detailonbj.getMovieRate(),
                            detailonbj.getMovieReleaseDate(),
                            detailonbj.getMovieOverview(),
                            detailonbj.getMovieReviews(),
                            detailonbj.getMovieImageUrl());
                     db.addContact(dbobj);

                    Toast.makeText(getContext(),"Done",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(),"Already Favourite",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    private void sendRequest2(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray reviewsArray = response.getJSONArray("results");
                        for (int j = 0; j < reviewsArray.length(); j++) {
                            JSONObject thereviewsObject = reviewsArray.getJSONObject(j);
                            reviews+=j+1+"."+newline;
                            reviews+="Author name : "+thereviewsObject.getString("author");
                            reviews+=newline;
                            reviews+=thereviewsObject.getString("content")+newline+newline;
                            Log.d("revi",reviews);
                            //MovieContent.setText(thereviewsObject.getString("content"));
                        }
                    if(reviews!="") {
                        detailonbj.setMovieReviews(reviews);
                        MovieAuthor.setText(reviews);
                    }
                    else{
                        detailonbj.setMovieReviews("No Reviews");
                        MovieAuthor.setText("No Reviews");
                    }
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
        requestQueue2.add(jsonObjectRequest);
    }
    private void sendRequest3(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray reviewsArray = response.getJSONArray("results");
                    for (int j = 0; j < reviewsArray.length(); j++) {
                        JSONObject thevideosObject = reviewsArray.getJSONObject(j);
                        trailerlist.add("https://www.youtube.com/watch?v="+thevideosObject.getString("key"));
                    }
                    ArrayAdapter itemsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, trailerlist);
                    MovieVideos.setAdapter(itemsAdapter);
                    MovieVideos= (ListView) Utility.setListViewHeightBasedOnChildren(MovieVideos);
                    MovieVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailerlist.get(position))));
                        }
                    });

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
        requestQueue3.add(jsonObjectRequest);
    }
}

    /*@Override
    protected void (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_a, container, false);
        return v;

    }


 @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("Fragment_App_Fragment", "Fragment onActivityCreated Called");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Fragment_App_Fragment", "Fragment onStart Called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Fragment_App_Fragment", "Fragment onResume Called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Fragment_App_Fragment", "Fragment onPause Called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Fragment_App_Fragment", "Fragment onStop Called");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Fragment_App_Fragment", "Fragment onDestroyView Called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Fragment_App_Fragment", "Fragment onDestroy Called");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Fragment_App_Fragment", "Fragment onDetach Called");
    */