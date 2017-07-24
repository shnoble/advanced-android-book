package com.shnoble.wordsoftoday;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static android.provider.BaseColumns._ID;
import static com.shnoble.wordsoftoday.WordsOfTodayContract.TABLE_NAME;
import static com.shnoble.wordsoftoday.WordsOfTodayDbHelper.DB_NAME;
import static com.shnoble.wordsoftoday.WordsOfTodayDbHelper.DB_VERSION;

public class WordOfTodayProvider extends ContentProvider {

    private static final String TAG = WordOfTodayProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher;

    public static final int ROW_DIR = 1;
    public static final int ROW_ITEM = 2;

    private WordsOfTodayDbHelper mDbHelper;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(WordsOfTodayContract.AUTHORITY, TABLE_NAME, ROW_DIR);
        sUriMatcher.addURI(WordsOfTodayContract.AUTHORITY, TABLE_NAME + "/#", ROW_ITEM);
    }

    public WordOfTodayProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "WordsOfTodayProvider onCreate");

        mDbHelper = new WordsOfTodayDbHelper(getContext(), DB_NAME, null, DB_VERSION, new DatabaseErrorHandler() {
            @Override
            public void onCorruption(SQLiteDatabase sqLiteDatabase) {
                Log.d(TAG, "onCorruption");

                String path = sqLiteDatabase.getPath();

                Context context = getContext();
                if (context != null) {
                    context.deleteFile(path);
                }
            }
        });
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (sUriMatcher.match(uri)) {
            case ROW_DIR:
                Log.d(TAG, "query(dir) uri=" + uri.toString());
                synchronized (mDbHelper) {
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                }
                return cursor;

            case ROW_ITEM:
                Log.d(TAG, "query(item) uri=" + uri.toString());
                synchronized (mDbHelper) {
                    long id = ContentUris.parseId(uri);
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    cursor = db.query(TABLE_NAME, projection, _ID, new String[] { Long.toString(id) }, null, null, null);
                }
                break;
            default:
                throw new IllegalArgumentException("인수의 URI가 틀렸습니다");
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
