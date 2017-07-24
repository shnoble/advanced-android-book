package com.shnoble.wordsoftoday;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.provider.BaseColumns._ID;
import static com.shnoble.wordsoftoday.WordsOfTodayContract.TABLE_NAME;
import static com.shnoble.wordsoftoday.WordsOfTodayContract.WordsOfTodayColumns.DATE;
import static com.shnoble.wordsoftoday.WordsOfTodayContract.WordsOfTodayColumns.NAME;
import static com.shnoble.wordsoftoday.WordsOfTodayContract.WordsOfTodayColumns.WORDS;

/**
 * Created by shhong on 2017. 7. 24..
 */

public class WordsOfTodayDbHelper extends SQLiteOpenHelper {

    public static final String TAG = WordsOfTodayDbHelper.class.getSimpleName();

    public static final String DB_NAME = "WordsOfToday.db";
    public static final int DB_VERSION = 1;

    private static final String SQL_CREATE_TABLE =
            String.format("CREATE TABLE %s (\n", TABLE_NAME) +
            String.format("%s INTEGER PRIMARY KEY AUTOINCREMENT,\n", _ID) +
            String.format("%s TEXT,\n", NAME) +
            String.format("%s TEXT,\n", WORDS) +
            String.format("%s TEXT);", DATE);

    private static final String[] SQL_INSERT_INITIAL_DATA = {
            String.format("INSERT INTO %s (%s, %s, %s)" + "VALUES('Taiki','날씨 참 좋다','20151001')", TABLE_NAME, NAME, WORDS, DATE),
            String.format("INSERT INTO %s (%s, %s, %s)"+"VALUES('Osamu','앱 버그 수정함','20151001')",TABLE_NAME,NAME,WORDS,DATE),
            String.format("INSERT INTO %s (%s, %s, %s)"+"VALUES('Osamu','오늘도 앱 버그 잡기','20151002')",TABLE_NAME,NAME,WORDS,DATE),
            String.format("INSERT INTO %s (%s, %s, %s)"+"VALUES('Taiki','열심히 운동함','20151002')",TABLE_NAME,NAME,WORDS,DATE),
            String.format("INSERT INTO %s (%s, %s, %s)"+"VALUES('Ken','머리 짧게 깎음','20151002')",TABLE_NAME,NAME,WORDS,DATE),
            String.format("INSERT INTO %s (%s, %s, %s)"+"VALUES('Taiki','오늘 점심 맛있네','20151003')",TABLE_NAME,NAME,WORDS,DATE),
            String.format("INSERT INTO %s (%s, %s, %s)"+"VALUES('Taiki','아침 4시 30분에 일어났다','20151004')",TABLE_NAME,NAME,WORDS,DATE),
    };

    public WordsOfTodayDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate");
        sqLiteDatabase.beginTransaction();
        try {
            execSQL(sqLiteDatabase, SQL_CREATE_TABLE);
            for (String sql : SQL_INSERT_INITIAL_DATA) {
                execSQL(sqLiteDatabase, sql);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade oldVersion=" + oldVersion + ", newVersion=" + newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.d(TAG, "onOpen");
    }

    private void execSQL(SQLiteDatabase sqLiteDatabase, String sql) {
        Log.d(TAG, "execSQL sql=" + sql);
        sqLiteDatabase.execSQL(sql);
    }
}
