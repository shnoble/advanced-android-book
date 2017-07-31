package com.shnoble.intentservicesample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private LocalBroadcastManager mLocalBroadcastManager;
    private IntentFilter mIntentFilter;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public static final String TAG = "LocalBroadcastReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (FibService.ACTION_CALC_DONE.equals(action)) {
                int result = intent.getIntExtra(FibService.KEY_CALC_RESULT, -1);
                long msec = intent.getLongExtra(FibService.KEY_CALC_MILLISECONDS, -2);

                Log.d(TAG, "Result: " + result);
                Log.d(TAG, "Milliseconds: " + msec);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        mIntentFilter = new IntentFilter(FibService.ACTION_CALC_DONE);

        Intent serviceIntent = new Intent(FibService.ACTION_CALC);
        serviceIntent.setClass(getApplicationContext(), FibService.class);
        startService(serviceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mLocalBroadcastManager.registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mLocalBroadcastManager.unregisterReceiver(mReceiver);
    }
}
