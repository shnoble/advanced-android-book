package com.shnoble.wordsoftoday;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.stetho.Stetho;

import static com.shnoble.wordsoftoday.WordsOfTodayContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String[] PROJECTIONS = new String[] {
            WordsOfTodayContract.WordsOfTodayColumns._ID,
            WordsOfTodayContract.WordsOfTodayColumns.NAME,
            WordsOfTodayContract.WordsOfTodayColumns.WORDS,
            WordsOfTodayContract.WordsOfTodayColumns.DATE,
    };

    private ContentResolver mContentResolver;
    private ContentObserver mObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (BuildConfig.DEBUG) {
            Context context = getApplicationContext();
            Stetho.initializeWithDefaults(this);
        }

        mContentResolver = getContentResolver();

        dump();

        mObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);

                Log.d(TAG, "(Observer) 데이터 변경이 있었습니다");
                dump();
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 두 번째 인수가 false인 경우 첫 번째 인수의 URI와 일치할 때만
        // ContentObserver.onChange()를 호출
        // true인 경우는 URI와 부분 일치
        mContentResolver.registerContentObserver(CONTENT_URI, true, mObserver);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mContentResolver.unregisterContentObserver(mObserver);
    }

    private void dump() {
        Cursor c = mContentResolver.query(CONTENT_URI, PROJECTIONS, null, null, null);
        if (c.moveToFirst()) {
            int wordsCol = c.getColumnIndexOrThrow(WordsOfTodayContract.WordsOfTodayColumns.WORDS);
            do {
                Log.d(TAG, "words=" + c.getString(wordsCol));
            } while(c.moveToNext());
        }
    }
}
