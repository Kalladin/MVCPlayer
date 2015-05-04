package com.kalladin.mvcplayer;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Kalladin on 2015/4/29.
 */
public class PlayerDetailFragment extends Fragment{

    final String LOG_TAG = "PlayerDetailFragment";

    private LayoutInflater mInflator;
    private TextView titleTv;
    private TextView artistTv;

    public static PlayerDetailFragment instance(Song song) {
        PlayerDetailFragment fragment = new PlayerDetailFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("id", song.getId());
        args.putString("title", song.getTitle());
        args.putString("artist", song.getArtist());
        args.putString("duration", song.getDurationFormat());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflator = inflater;
        View v = inflater.inflate(R.layout.detail_fragment, container, false);
        titleTv = (TextView)v.findViewById(R.id.detail_titleTv);
        artistTv = (TextView)v.findViewById(R.id.detail_artistTv);
        Bundle args = getArguments();
        if(args != null) {
            titleTv.setText(args.getString("title"));
            artistTv.setText(args.getString("artist"));
        }
        return v;
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy()");
        super.onDestroy();
    }
}
