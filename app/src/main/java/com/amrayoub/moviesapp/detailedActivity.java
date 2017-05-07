package com.amrayoub.moviesapp;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class detailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        MovieInfo detailonbj;
        detailonbj = (MovieInfo) getIntent().getSerializableExtra("data");
        Holder.setInput(detailonbj);
        setTitle(detailonbj.getMovieName());
        detaildFragment fragmentA = new detaildFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_detailed,fragmentA, "").commit();
    }
}
