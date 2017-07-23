package com.shnoble.contentprovider.sample;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Cursor cursor = getImage();
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
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                Log.d(TAG, "id: " + id);
                Log.d(TAG, "title: " + title);
                Log.d(TAG, "dateTaken: " + dateTaken);
                Log.d(TAG, "imageUri: " + imageUri);

                // 3. 데이터를 View로 설정
                TextView textView = (TextView) findViewById(R.id.textView);
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                Calendar calendar = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(dateTaken);
                    String text = DateFormat.format("yyyy/MM/dd(E) kk:mm:ss", calendar).toString();
                    textView.setText("촬영일시 " + text);
                }
                imageView.setImageURI(imageUri);
            }
            cursor.close();

        } catch (SecurityException e) {
            Toast.makeText(this, "스토리지에 접근 권한을 허가로 해주세요.（종료합니다)", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private Cursor getImage() {
        ContentResolver contentResolver = getContentResolver();
        Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // 가져올 컬럼명
        String[] projection = {
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.TITLE,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
        };

        // 정렬
        String sortOrder = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";

        // 1건만 가져옴
        queryUri = queryUri.buildUpon().appendQueryParameter("limit", "1").build();

        // selection, selectionArgs는 지정하지 않는다.
        return contentResolver.query(queryUri, projection, null, null, sortOrder);
    }
}
