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
import static com.shnoble.wordsoftoday.WordsOfTodayContract.MIME_TYPE_DIR;
import static com.shnoble.wordsoftoday.WordsOfTodayContract.MIME_TYPE_ITEM;
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
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case ROW_ITEM:
                int id =  (int) ContentUris.parseId(uri);
                synchronized (mDbHelper) {
                    Log.d(TAG, "delete(item) id=" + id);
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    count = db.delete(TABLE_NAME, _ID+"=?", new String[]{ Long.toString(id) });
                    if (count > 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                }
                break;
            case ROW_DIR:
                synchronized (mDbHelper) {
                    Log.d(TAG, "delete(dir)");
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    count = db.delete(TABLE_NAME, selection, selectionArgs);
                    if (count > 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("인수의 URI가 틀렸습니다.");
        }
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case ROW_DIR:
                return MIME_TYPE_DIR;
            case ROW_ITEM:
                return MIME_TYPE_ITEM;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri resultUri = null;
        switch (sUriMatcher.match(uri)) {
            case ROW_DIR:
                synchronized (mDbHelper) {
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    long lastId = db.insert(TABLE_NAME, null, values);
                    resultUri = ContentUris.withAppendedId(uri, lastId);
                    Log.d(TAG, "WordsOfTodayProvider insert " + values);

                    // 변경 통지
                    Context context = getContext();
                    if (context != null) {
                        context.getContentResolver().notifyChange(resultUri, null);
                    }
                }
                break;
            default:
                throw  new IllegalArgumentException("인수의 URI가 틀렸습니다");
        }
        return resultUri;
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
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case ROW_ITEM:
                Log.d(TAG, "update(item) values " + values);
                int id = (int) ContentUris.parseId(uri);
                synchronized (mDbHelper) {
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    count = db.update(TABLE_NAME, values, _ID + "=?", new String[] { Long.toString(id) });
                    if (count > 0) {
                        // 변경통지
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                }
                break;
            case ROW_DIR:
                Log.d(TAG, "update(dir) values=" + values);
                synchronized (mDbHelper) {
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    count = db.update(TABLE_NAME, values, selection, selectionArgs);
                    if (count > 0) {
                        // 변경통지
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                }
            default:
                throw new IllegalArgumentException("인수의 URI가 틀렸습니다");
        }
        return count;
    }
}
