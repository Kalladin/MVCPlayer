package com.kalladin.mvcplayer;

import android.widget.Toast;

/**
 * Created by Kalladin on 2015/4/30.
 */
public class PlayerFakeController implements PlayerControllerInterface {

    PlayerViewActivity activity;

    public PlayerFakeController(PlayerViewActivity activity, PlayerModelInterface model) {
        this.activity = activity;
    }

    @Override
    public void init() {

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

    @Override
    public void start() {
        Toast.makeText(activity.getBaseContext(),getClass().getName() + " is unable to start player.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void stop() {
        Toast.makeText(activity.getBaseContext(),getClass().getName() + " is unable to stop player.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void pause() {
        Toast.makeText(activity.getBaseContext(),getClass().getName() + " is unable to pause player.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refresh() {
        Toast.makeText(activity.getBaseContext(),getClass().getName() + " is unable to get music list.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void pickMusic(Song song) {
        Toast.makeText(activity.getBaseContext(),getClass().getName() + " is unable to play music.", Toast.LENGTH_SHORT).show();
    }
}
