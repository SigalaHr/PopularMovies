package com.example.android.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String title;
    private String overview;
    private String posterImagePath;
    private String releaseDate;
    private Double voteAverage;
    private static final String DATE_LAYOUT = "yyyy-MM-dd";

    public Movie() {
    }

    public void setTitle(String t) {
        title = t;
    }
    public void setOverview(String o) {
        if(!o.equals("null")) {
            this.overview = o;
        }
    }
    public void setPosterImagePath(String pip) {
        posterImagePath = pip;
    }
    public void setReleaseDate(String ra) {
        if(!ra.equals("null")) {
            this.releaseDate = ra;
        }
    }
    public void setVoteAverage(Double va) {
        this.voteAverage = va;
    }

    public String getTitle() {
        return title;
    }
    public String getOverview() {
        return overview;
    }
    public String getPosterImagePath() {
        final String POSTER_URL_TMDB = "https://image.tmdb.org/t/p/w185";

        return POSTER_URL_TMDB + posterImagePath;
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    private Double getVoteAverage() {
        return voteAverage;
    }
    public String getVoteAverageOutOfTen() {
        return String.valueOf(getVoteAverage()) + "/10";
    }
    public String getDate() {
        return DATE_LAYOUT;
    }

    @Override
    public void writeToParcel(Parcel par, int i) {
        par.writeString(title);
        par.writeString(overview);
        par.writeString(posterImagePath);
        par.writeString(releaseDate);
        par.writeValue(voteAverage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel p) {
        title = p.readString();
        overview = p.readString();
        posterImagePath = p.readString();
        releaseDate = p.readString();
        voteAverage = (Double) p.readValue(Double.class.getClassLoader());
    }

}
