package com.example.android.myapplication;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.myapplication.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MovieAsyncTask extends AsyncTask<String, Void, Movie[]> {

    private final String TAG = MovieAsyncTask.class.getSimpleName();
    private final String APIKEY;
    private final OnTaskComplete LISTENER;
    private String jsonString = null;

    public MovieAsyncTask(OnTaskComplete taskComplete, String key) {
        super();

        LISTENER = taskComplete;
        APIKEY = key;
    }

    @Override
    protected Movie[] doInBackground(String... param) {
        HttpURLConnection connectHttpURL = null;
        BufferedReader buffReader = null;

        try {
            URL url = getApiUrl(param);
            connectHttpURL = (HttpURLConnection) url.openConnection();
            connectHttpURL.setRequestMethod("GET");
            connectHttpURL.connect();

            InputStream inputS = connectHttpURL.getInputStream();
            StringBuilder stringBuild = new StringBuilder();

            if (inputS == null) {
                return null;
            }
            buffReader = new BufferedReader(new InputStreamReader(inputS));

            String newLine;
            while ((newLine = buffReader.readLine()) != null) {
                stringBuild.append(newLine).append("\n");
            }

            if (stringBuild.length() == 0) {
                return null;
            }

            jsonString = stringBuild.toString();
        } catch (IOException ex) {
            Log.e(TAG, String.valueOf(R.string.error), ex);
            return null;
        } finally {
            if (connectHttpURL != null) {
                connectHttpURL.disconnect();
            }
            if (buffReader != null) {
                try {
                    buffReader.close();
                } catch (final IOException ex) {
                    Log.e(TAG, String.valueOf(R.string.closing_error), ex);
                }
            }
        }
        try {
            return getMoviesInfoFromJson(jsonString);
        } catch (JSONException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            ex.printStackTrace();
        }

        return null;
    }

    private Movie[] getMoviesInfoFromJson(String jsonString) throws JSONException {
        final String RESULTS = "results";
        final String TITLE = "title";
        final String OVERVIEW = "overview";
        final String POSTER_IMAGE_PATH = "poster_path";
        final String RELEASE_DATE = "release_date";
        final String VOTE_AVERAGE = "vote_average";

        JSONObject movieJson = new JSONObject(jsonString);
        JSONArray resultsArray = movieJson.getJSONArray(RESULTS);

        Movie[] movies = new Movie[resultsArray.length()];

        for (int i = 0; i < resultsArray.length(); i++) {
            movies[i] = new Movie();

            JSONObject movieInfo = resultsArray.getJSONObject(i);

            movies[i].setTitle(movieInfo.getString(TITLE));
            movies[i].setPosterImagePath(movieInfo.getString(POSTER_IMAGE_PATH));
            movies[i].setOverview(movieInfo.getString(OVERVIEW));
            movies[i].setVoteAverage(movieInfo.getDouble(VOTE_AVERAGE));
            movies[i].setReleaseDate(movieInfo.getString(RELEASE_DATE));
        }
        return movies;
    }

    private URL getApiUrl(String[] parameters) throws MalformedURLException {
        final String URL_BEGINNING = "https://api.themoviedb.org/3/movie";
        final String API_KEY = "api_key";

        Uri newUri = Uri.parse(URL_BEGINNING).buildUpon()
                .appendPath(parameters[0])
                .appendQueryParameter(API_KEY, APIKEY)
                .build();

        return new URL(newUri.toString());
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);

        LISTENER.onMovieTaskCompleted(movies);
    }
}
