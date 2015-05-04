package com.kalladin.mvcplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Kalladin on 2015/4/28.
 */
public class PlayerModel implements  PlayerModelInterface {
    final String TAG = "PlayerModel";
    private ArrayList<PlayerObserverInterface> observers;
    private ArrayList<Song> songList;
    private Context mContext;
    private MediaPlayer mp;
    private Song currentSong;
    private boolean isStop = false;
    private float volumeBeforeMute;

    public PlayerModel(Context context) {
        observers = new ArrayList<PlayerObserverInterface>();
        songList = new ArrayList<Song>();
        this.mContext = context;
        mp = new MediaPlayer();
    }

    @Override
    public void unregister(PlayerObserverInterface observer) {
        observers.remove(observer);
    }


    @Override
    public void start() {
        if(mp != null){
            try {
                if(isStop) {
                    mp.prepare();
                    isStop = false;
                }
                mp.start();
            } catch (IllegalStateException e) {
                Log.e(TAG, "IllegalStateException() - " + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {
        if(mp != null) {
            isStop = true;
            mp.seekTo(0);
            mp.stop();
        }
    }

    @Override
    public void pause() {
        if(mp != null)
            mp.pause();
    }

    @Override
    public void register(PlayerObserverInterface observer) {
        observers.add(observer);
    }

    private void onDataChanged() {
        Log.d(TAG, "onDataChanged() - notify all observers");
        Bundle data = new Bundle();
        data.putString("command", "CMD_REFRESH_DATA");
        data.putSerializable("data", songList);

        for (PlayerObserverInterface observer : observers) {
            observer.update(data);
        }
    }

    public void loadMusicData() {
        Log.d(TAG, "loadMusicData() - LOADING ...");
        songList.clear();
        ContentResolver musicResolver = mContext.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if(musicCursor != null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int durationColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DURATION);
            int pathColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DISPLAY_NAME);
            int mimeColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.MIME_TYPE);
            int dataColumn = musicCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            //add songs to list
            do {
                int thisId = musicCursor.getInt(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                int thisDuration = musicCursor.getInt(durationColumn);
                Log.d(TAG, "MIME:" +musicCursor.getString(mimeColumn));
                String data = musicCursor.getString(dataColumn);
                Uri uri = Uri.parse(data);
                Log.d(TAG, "PATH:" +uri.toString());
                songList.add(new Song(thisId, thisTitle, thisArtist, thisDuration, uri));
            }
            while (musicCursor.moveToNext());
        }
        musicCursor.close();

        for(Song s : songList)
            s.print();
        Log.d(TAG, "loadMusicData() - END ...");
        onDataChanged();
    }

    @Override
    public void setCurrentSong(Song song) {
        currentSong = song;
        try {
            mp.reset();
            mp.setDataSource(mContext, currentSong.getFilePath());
            mp.prepare();
            isStop = false;
        } catch (IOException e) {
            Log.e(TAG, "IOException() - " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void setVolume(float volume) {
        Log.d(TAG, "[TEST]setVolume() - volume:" + volume);
        AudioManager am = (AudioManager) mContext.getSystemService(mContext.AUDIO_SERVICE);

        am.setStreamVolume(AudioManager.STREAM_MUSIC,
                (int)volume, 0);
        /*if(mp != null) {
            mp.setVolume(volume, volume);
        }*/
    }

    @Override
    public int getVolume() {
        AudioManager am = (AudioManager) mContext.getSystemService(mContext.AUDIO_SERVICE);
        return am.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void toggleMuteUnmute() {
        float tmpVolume = (float) (getVolume());
        Log.d(TAG,"[TEST]toggleMuteUnmute() - current vol:"+tmpVolume);
        if(tmpVolume == 0) {
            setVolume(volumeBeforeMute);
        } else {
            volumeBeforeMute = tmpVolume;
            setVolume(0);
            Log.d(TAG,"[TEST]toggleMuteUnmute() - SAVE Vol before mute vol:"+volumeBeforeMute);
        }
    }
}
