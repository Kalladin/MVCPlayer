package com.kalladin.mvcplayer;

import android.os.Bundle;
import android.util.Log;

/**
 * Created by Kalladin on 2015/4/28.
 */
public class PlayerController implements PlayerControllerInterface, PlayerObserverInterface {

    PlayerViewActivity mActivity;
    PlayerModelInterface mModel;

    final String TAG = getClass().getName();
    boolean isMuted = false;

    public PlayerController(PlayerViewActivity activity, PlayerModelInterface model) {
        this.mActivity = activity;
        this.mModel = model;
        init();
    }

    @Override
    public void init() {
        mModel.register(this);
    }

    @Override
    public void start() {
        mModel.start();
    }

    @Override
    public void stop() {
        mModel.stop();
    }

    @Override
    public void refresh() {
        mModel.loadMusicData();
    }

    @Override
    public void pause() {
        mModel.pause();
    }

    @Override
    public void pickMusic(Song song) {
        mModel.setCurrentSong(song);
        mModel.start();
    }

    @Override
    public void setVolume(float volume) {
        mModel.setVolume(volume);
        if(volume == 0)
            mActivity.setMutedUnmuted(true);
        else
            mActivity.setMutedUnmuted(false);
    }

    @Override
    public int getVolume() {
        int volume = mModel.getVolume();
        Log.d(TAG, "getVolume() - volume:" + volume);
        mActivity.setVolumeSeekbarValue(volume);
        if(volume == 0) {
            mActivity.setMutedUnmuted(true);
        } else {
            mActivity.setMutedUnmuted(false);
        }
        return volume;
    }

    @Override
    public void toggleMuteUnmute() {
        mModel.toggleMuteUnmute();
        isMuted = !isMuted;
        if(isMuted) {
            mActivity.setVolumeSeekbarValue(0);
            mActivity.setMutedUnmuted(true);
        } else {
            mActivity.setVolumeSeekbarValue(mModel.getVolume());
            mActivity.setMutedUnmuted(false);
        }
    }

    @Override
    public void update(Bundle data) {
        if (data != null) {
            String command = data.getString("command");
            Log.d(TAG, "Received observable notify:" + command);
        }
    }
}
