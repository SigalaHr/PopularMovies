package com.example.android.myapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.myapplication.models.Movie;
import com.squareup.picasso.Picasso;

public class MovieImageAdapter extends BaseAdapter {
    private final Context context;
    private final Movie[] movies;

    public MovieImageAdapter(Context c, Movie[] m) {
        context = c;
        movies = m;
    }

    @Override
    public int getCount() {
        if (movies == null || movies.length == 0) {
            return -1;
        }
        return movies.length;
    }

    @Override
    public Movie getItem(int pos) {
        if (movies == null || movies.length == 0) {
            return null;
        }
        return movies[pos];
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        ImageView movieView;

        if (view == null) {
            movieView = new ImageView(context);
            movieView.setAdjustViewBounds(true);
        } else {
            movieView = (ImageView) view;
        }

        Picasso.with(context)
                .load(movies[pos].getPosterImagePath())
                .resize(180, 275)
                .placeholder(R.drawable.loading)
                .error(R.drawable.not_found)
                .into(movieView);

        return movieView;
    }
}
