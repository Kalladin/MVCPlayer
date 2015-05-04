package com.kalladin.mvcplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Kalladin on 2015/4/30.
 */
public class PlayerFakeModel implements PlayerModelInterface {
    ArrayList<PlayerObserverInterface> observers;
    ArrayList<Song> songList;

    public PlayerFakeModel(Context context) {
        observers = new ArrayList<PlayerObserverInterface>();
        songList = new ArrayList<Song>();
    }

    @Override
    public void register(PlayerObserverInterface observer) {
        observers.add(observer);
    }

    @Override
    public void unregister(PlayerObserverInterface observer) {
        observers.remove(observer);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void loadMusicData() {
        Bundle joke = new Bundle();
        songList.clear();
        songList.add(new Song(0,"You are AWESOME", "Steven", 0 , null));
        joke.putString("command", "CMD_REFRESH_DATA");
        joke.putSerializable("data", songList);

        for(PlayerObserverInterface observer : observers)
            observer.update(joke);
    }

    @Override
    public void setCurrentSong(Song song) {

    }

    @Override
    public void setVolume(float volume) {

    }

    @Override
    public int getVolume() {
        return 0;
    }

    @Override
    public void toggleMuteUnmute() {

    }
}
