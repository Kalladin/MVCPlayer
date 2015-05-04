package com.kalladin.mvcplayer;

import android.net.Uri;
import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Song implements Serializable {

    final String TAG = "Song";
    int id;
    int duration;
    String title;
    String artist;
    Uri filePath;

    public Song(int id, String title, String artist, int duration, Uri filePath) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getArtist() {
        return artist;
    }
    public int getDuration() {
        return duration;
    }
    public Uri getFilePath() {
        return filePath;
    }

    public String getDurationFormat() {
        if(Math.round(duration / 3600000) > 0) {
            SimpleDateFormat df = new SimpleDateFormat("kk:mm:ss");
            return df.format(new Date(duration));
        }
        SimpleDateFormat df = new SimpleDateFormat("mm:ss");
        return df.format(new Date(duration));
    }

    public void print() {
        Log.d(TAG, "(" + id + "," + title + "," + artist + ","+ getDurationFormat() + "," + filePath.toString() + ")");
    }
}
