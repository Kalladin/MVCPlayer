package com.kalladin.mvcplayer;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;


public class PlayerViewActivity extends Activity implements View.OnClickListener,
        PlayerObserverInterface, PlayerListFragment.OnSongsSelectedListener {
    FrameLayout fragmentContainer;
    LinearLayout functionBar;
    ImageButton startBtn;
    ImageView stopBtn;
    ImageView pauseBtn;
    ImageView speakerView;
    SeekBar volumeSeekbar;

    PlayerControllerInterface mPlayerController;
    PlayerModelInterface mModel;

    PlayerListFragment listFragment;
    PlayerDetailFragment detailFragment;

    final String TAG = "PlayerViewActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        initUI();
        initController();
        //switchController(); //DEMO, we can switch controller easily [Strategy Pattern]
    }

    @Override
    protected void onDestroy() {
        mModel.unregister(this);
        super.onDestroy();
    }

    private void initUI() {
        fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
        functionBar = (LinearLayout) findViewById(R.id.functionBar);
        volumeSeekbar = (SeekBar) findViewById(R.id.volumeSeekbar);
        volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "SeekBar onProgressChanged() - progress:" + (progress));
                if(mPlayerController != null)
                    mPlayerController.setVolume(((float)progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volumeSeekbar.setMax(audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        Log.d(TAG,"[TEST] MAX VOLUME"+audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        startBtn = (ImageButton) findViewById(R.id.startBtn);
        stopBtn = (ImageView) findViewById(R.id.stopBtn);
        pauseBtn = (ImageView) findViewById(R.id.pauseBtn);
        speakerView = (ImageView) findViewById(R.id.speakerView);
        startBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        pauseBtn.setOnClickListener(this);
        speakerView.setOnClickListener(this);
        listFragment = new PlayerListFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, listFragment);
        transaction.commit();
    }

    private void initController() {
        mModel = new PlayerModel(getBaseContext());
        mModel.register(this);
        //switchModel(); //DEMO, we can switch model dynamically [Observer Pattern]
        mPlayerController = new PlayerController(this, mModel);
        mPlayerController.getVolume();
    }
    /* ------------ DEMO USAGE ------------ */
    private void switchController() {
        mPlayerController = new PlayerFakeController(this, mModel);
    }
    private void switchModel() {
        mModel = new PlayerFakeModel(getBaseContext());
        mModel.register(this);
    }
    /* ------------------------------------ */

    public void setVolumeSeekbarValue(int volume) {
        if(volumeSeekbar != null) {
            Log.d(TAG, "setVolumeSeekbarValue() - set volume:" + volume);
            volumeSeekbar.setProgress(volume);
        }
    }

    public void setMutedUnmuted(boolean mute) {
        if(mute)
            speakerView.setImageResource(R.drawable.speaker_muted);
        else
            speakerView.setImageResource(R.drawable.speaker);
    }

    public void hideFunctionBar() {
        functionBar.setVisibility(View.GONE);
    }
    public void showFunctionBar() {
        functionBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void update(Bundle data) {
        if (data != null) {
            String command = data.getString("command");
            Log.d(TAG, "Received observable notify:" + command);
            if(command.contentEquals("CMD_REFRESH_DATA")) {
                listFragment.addData((ArrayList<Song>) data.getSerializable("data"));
            }
        }
    }

    @Override
    public void onSongsSelected(Song song) {
        if(song.getFilePath() == null) {
            Toast.makeText(getBaseContext(),"This song is not exist!", Toast.LENGTH_LONG).show();
            return;
        }
        showFunctionBar();
        mPlayerController.pickMusic(song);
        detailFragment = PlayerDetailFragment.instance(song);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onPlayerListFragmentDone() {
        hideFunctionBar();
        mPlayerController.refresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startBtn:
                mPlayerController.start();
                break;
            case R.id.pauseBtn:
                mPlayerController.pause();
                break;
            case R.id.stopBtn:
                mPlayerController.stop();
                break;
            case R.id.speakerView:
                mPlayerController.toggleMuteUnmute();
        }
    }
}
