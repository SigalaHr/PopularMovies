package com.example.android.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.android.myapplication.models.Movie;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.grid_view);
        gridView.setOnItemClickListener(posterClick);

        if (savedState == null) {
            getMovies(howToSort());
        } else {
            Parcelable[] parcelable = savedState.
                    getParcelableArray(getString(R.string.movie_par));

            if (parcelable != null) {
                int movieObjects = parcelable.length;
                Movie[] movies = new Movie[movieObjects];
                for (int i = 0; i < movieObjects; i++) {
                    movies[i] = (Movie) parcelable[i];
                }
                gridView.setAdapter(new MovieImageAdapter(this, movies));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        switch (itemId) {
            case R.id.popular:
                editor.putString(getString(R.string.sort_method), getString(R.string.sort_by_popular_desc));
                editor.apply();
                getMovies(howToSort());
                return true;
            case R.id.top_rated:
                editor.putString(getString(R.string.sort_method), getString(R.string.sort_by_average_desc));
                editor.apply();
                getMovies(howToSort());
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private final GridView.OnItemClickListener posterClick = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
            Movie movie = (Movie) parent.getItemAtPosition(pos);

            Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);
            intent.putExtra(getResources().getString(R.string.movie_par), movie);

            startActivity(intent);
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int numMovies = gridView.getCount();
        if (numMovies > 0) {
            Movie[] movies = new Movie[numMovies];
            for (int i = 0; i < numMovies; i++) {
                movies[i] = (Movie) gridView.getItemAtPosition(i);
            }
            outState.putParcelableArray(getString(R.string.movie_par), movies);
        }
        super.onSaveInstanceState(outState);
    }

    private void getMovies(String sort) {
        if (checkNetwork()) {
            String apiKey = getString(R.string.api_key);

            OnTaskComplete taskCompleted = new OnTaskComplete() {
                @Override
                public void onMovieTaskCompleted(Movie[] movies) {
                    gridView.setAdapter(new MovieImageAdapter(getApplicationContext(), movies));
                }
            };
            MovieAsyncTask movieTask = new MovieAsyncTask(taskCompleted, apiKey);
            movieTask.execute(sort);
        } else {
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    private String howToSort() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(getString(R.string.sort_method),
                getString(R.string.sort_by_popular_desc));
    }

    private boolean checkNetwork() {
        ConnectivityManager conMan
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkStatus = conMan.getActiveNetworkInfo();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            networkStatus = Objects.requireNonNull(conMan).getActiveNetworkInfo();
        }
        return networkStatus != null && networkStatus.isConnected();
    }

}
