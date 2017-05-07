package com.amrayoub.moviesapp;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements NameListener{
    boolean mIsTwoPane = false;
    Mainfragment fragmentB;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentB = new Mainfragment();
        fragmentB.setNameListener(this);
        getSupportActionBar().setTitle("Most Popular");
        getSupportFragmentManager().beginTransaction().add(R.id.flMain, fragmentB, "").commit();
        if (null != findViewById(R.id.flDetails)) {
        mIsTwoPane=true;
        }
    }
    @Override
    public void setSelectedName(MovieInfo name) {
        // Case One Pane
        //Start Details Activity
        if (!mIsTwoPane) {
            Intent i = new Intent(this, detailedActivity.class);
            i.putExtra("data",name);
            startActivity(i);
        } else {
            //Case Two-PAne
            detaildFragment mDetailsFragment= new detaildFragment();
            Holder.setInput(name);
            getSupportFragmentManager().beginTransaction().replace(R.id.flDetails,mDetailsFragment,"").commit();
        }
    }
    public void choose_sort(View view) {
        fragmentB.choose_sort(view);
}
}
