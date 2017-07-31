package com.example.musicplayerservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.nfc.Tag;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mPlayButton;
    private Button mStopButton;

    private MusicPlayerService mMusicPlayerService;

    private ServiceConnection mMusicPlayerServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d("ServiceConnection", "onServiceConnected");
            mMusicPlayerService = ((MusicPlayerService.MusicPlayerBinder)binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("ServiceConnection", "onServiceDisconnected");
            mMusicPlayerService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayButton = (Button) findViewById(R.id.play_button);
        mStopButton = (Button) findViewById(R.id.stop_button);

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMusicPlayerService != null) {
                    mMusicPlayerService.play();
                }
            }
        });

        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMusicPlayerService != null) {
                    mMusicPlayerService.stop();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mMusicPlayerService == null) {
            onBindService();
        }
        startService(new Intent(getApplicationContext(), MusicPlayerService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mMusicPlayerService != null) {
            if (!mMusicPlayerService.isPlaying()) {
                mMusicPlayerService.stopSelf();
            }
            unbindService(mMusicPlayerServiceConnection);
            mMusicPlayerService = null;
        }
    }

    private void onBindService() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        bindService(intent, mMusicPlayerServiceConnection, Context.BIND_AUTO_CREATE);
    }
}
