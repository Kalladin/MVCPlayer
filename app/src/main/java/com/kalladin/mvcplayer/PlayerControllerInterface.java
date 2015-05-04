package com.kalladin.mvcplayer;

/**
 * Created by Kalladin on 2015/4/28.
 */
public interface PlayerControllerInterface {
    public void init();
    public void setVolume(float volume);
    public int  getVolume();
    public void toggleMuteUnmute();
    public void start();
    public void stop();
    public void pause();
    public void refresh();
    public void pickMusic(Song song);
}
