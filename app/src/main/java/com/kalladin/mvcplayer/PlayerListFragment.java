package com.kalladin.mvcplayer;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Kalladin on 2015/4/29.
 */
public class PlayerListFragment extends ListFragment {
    final String TAG = "PlayerListFragment";
    OnSongsSelectedListener mListener;
    SongListAdapter mAdapter;
    int selectedIndex = -1;

    public interface OnSongsSelectedListener {
        public void onPlayerListFragmentDone();
        public void onSongsSelected(Song selectedSong);
    }

    public PlayerListFragment() {

    }
    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG, "onAttach()");
        super.onAttach(activity);
        try {
            mListener = (OnSongsSelectedListener) activity;
            mAdapter = new SongListAdapter(activity.getBaseContext(), R.layout.song_listitem);
            setListAdapter(mAdapter);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated()");

        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            selectedIndex = savedInstanceState.getInt("curChoice", -1);
            if(selectedIndex >= 0)
                mListener.onSongsSelected(mAdapter.getItem(selectedIndex));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", selectedIndex);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDividerHeight(1);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        selectedIndex = position;
        mListener.onSongsSelected(mAdapter.getItem(position));
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        mListener.onPlayerListFragmentDone();
    }

    public void addData(ArrayList<Song> songList) {
        if(songList == null || songList.size() == 0) {
            Log.e(TAG, "addData() - songList is null or empty");
            return;
        }
        if(mAdapter == null) {
            Log.e(TAG, "addData() - adapter is not initialized yet");
            return;
        }
        Log.d(TAG, "addData() - addAll");
        mAdapter.clear();
        mAdapter.addAll(songList);
    }
}
