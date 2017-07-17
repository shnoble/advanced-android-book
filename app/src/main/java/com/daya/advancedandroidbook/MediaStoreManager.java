package com.daya.advancedandroidbook;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by shnoble on 2017-07-17.
 */

public class MediaStoreManager {

    public Cursor getImage(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
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
