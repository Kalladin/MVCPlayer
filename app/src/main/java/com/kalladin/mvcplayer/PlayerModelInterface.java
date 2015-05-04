package com.kalladin.mvcplayer;

/**
 * Created by Kalladin on 2015/4/28.
 */
public interface PlayerModelInterface {
    public void register(PlayerObserverInterface observer);
    public void unregister(PlayerObserverInterface observer);
    public void start();
    public void stop();
    public void pause();
    public void loadMusicData();
    public void setCurrentSong(Song song);
    public void setVolume(float volume);
    public int  getVolume();
    public void toggleMuteUnmute();
}
