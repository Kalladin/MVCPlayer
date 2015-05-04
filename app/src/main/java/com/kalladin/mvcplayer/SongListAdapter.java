package com.kalladin.mvcplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class SongListAdapter extends ArrayAdapter<Song> {

    //ArrayList<Song> songList;

    public SongListAdapter(Context context, int resource, ArrayList<Song> songList) {
        super(context, resource, songList);
        //this.songList = songList;
    }

    public SongListAdapter(Context context, int resource) {
        super(context, resource);
        //songList = new ArrayList<Song>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View v = convertView;

        if(v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.song_listitem, parent, false);
            TextView titleTv = (TextView)v.findViewById(R.id.titleTv);
            TextView artistTv = (TextView)v.findViewById(R.id.artistTv);
            TextView durationTv = (TextView)v.findViewById(R.id.durationTv);
            holder = new ViewHolder(titleTv, artistTv, durationTv);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.titleTv.setText(getItem(position).getTitle());
        holder.artistTv.setText(getItem(position).getArtist());
        holder.durationTv.setText(getItem(position).getDurationFormat());

        return v;
    }

    class ViewHolder {
        TextView titleTv;
        TextView artistTv;
        TextView durationTv;

        public ViewHolder(TextView titleTv, TextView artistTv, TextView durationTv){
            this.titleTv = titleTv;
            this.artistTv = artistTv;
            this.durationTv = durationTv;
        }
    }
}
