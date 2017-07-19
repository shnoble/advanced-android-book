package com.daya.advancedandroidbook;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MediaStoreManager mMediaStoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMediaStoreManager = new MediaStoreManager();
        Cursor cursor = mMediaStoreManager.getImage(getApplicationContext());
        if (cursor.moveToFirst()) {
            // 1. 각 칼럼의 열 인덱스 취득
            int idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);
            int titleColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.TITLE);
            int dateTakenColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN);

            Log.d(TAG, "idColumnIndex: " + idColumnIndex);
            Log.d(TAG, "titleColumnIndex: " + titleColumnIndex);
            Log.d(TAG, "dateTakenColumnIndex: " + dateTakenColumnIndex);
            
            // 2. 인덱스를 바탕으로 데이터를 Cursor로 부터 취득
            long id = cursor.getLong(idColumnIndex);
            String title = cursor.getString(titleColumnIndex);
            long dateTaken = cursor.getLong(dateTakenColumnIndex);
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

            Log.d(TAG, "id: " + id);
            Log.d(TAG, "title: " + title);
            Log.d(TAG, "dateTaken: " + dateTaken);
            Log.d(TAG, "imageUri: " + imageUri);
        }
        cursor.close();
    }
}
