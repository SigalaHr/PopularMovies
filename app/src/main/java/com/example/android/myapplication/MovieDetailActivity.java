package com.example.android.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myapplication.models.Movie;
import com.squareup.picasso.Picasso;

import java.text.ParseException;

public class MovieDetailActivity extends AppCompatActivity {

    TextView title;
    TextView overview;
    ImageView viewPosterImage;
    TextView releaseDate;
    TextView voteAverage;

    private final String TAG = MovieDetailActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        title = findViewById(R.id.view_title);
        viewPosterImage = findViewById(R.id.poster_image);
        overview = findViewById(R.id.overview);
        voteAverage = findViewById(R.id.vote_average);
        releaseDate = findViewById(R.id.release_date);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(getString(R.string.movie_par));

        title.setText(movie.getTitle());

        Picasso.with(this)
                .load(movie.getPosterImagePath())
                .resize(180, 275)
                .placeholder(R.drawable.loading)
                .error(R.drawable.not_found)
                .into(viewPosterImage);

        String overView = movie.getOverview();
        if (overView == null) {
            overview.setTypeface(null, Typeface.ITALIC);
            overView = getResources().getString(R.string.no_summary);
        }
        overview.setText(overView);
        voteAverage.setText(movie.getVoteAverageOutOfTen());

        String releaseDate = movie.getReleaseDate();
        if(releaseDate != null) {
            try {
                releaseDate = DateHelper.getLocalDate(this,
                        releaseDate, movie.getDate());
            } catch (ParseException e) {
                Log.e(TAG, getResources().getString(R.string.release_date_error), e);
            }
        } else {
            this.releaseDate.setTypeface(null, Typeface.ITALIC);
            releaseDate = getResources().getString(R.string.no_release_date);
        }
        this.releaseDate.setText(releaseDate);
    }
}
