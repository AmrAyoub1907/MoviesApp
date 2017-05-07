package com.amrayoub.moviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

/**
 * Created by Amr Ayoub on 21/10/2016.
 */

public class GridViewAdapter extends BaseAdapter{
    private Context mcontext;
    private int mlayoutid;
    ArrayList<MovieInfo> data = new ArrayList<>();
    public GridViewAdapter(Context context, int layoutResourceId, ArrayList<MovieInfo> data) {
        this.mlayoutid = layoutResourceId;
        this.mcontext = context;
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageview ;
        LayoutInflater inflater = (LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(mlayoutid, parent, false);
        imageview = (ImageView) convertView.findViewById(R.id.posterId);
        Picasso.with(mcontext).load(data.get(position).getMovieImageUrl()).into(imageview);
        return convertView;
    }
}
