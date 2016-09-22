package com.example.eslam.popular_movies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.eslam.popular_movies.db.MovieContracts.MOVIES_TABLE;
/**
 * Created by eslam on 20/08/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "popularmovie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_MOVIE = "CREATE TABLE " + MOVIES_TABLE.TABLE_NAME + "(" +
                MovieContracts.MOVIES_TABLE._ID + " INTEGER PRIMARY KEY," +
                MOVIES_TABLE.COLUMN_TITLE + " TEXT NOT NULL," +
                MOVIES_TABLE.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                MOVIES_TABLE.COLUMN_POSTER_IMAGE + " TEXT NOT NULL," +
                MOVIES_TABLE.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                MOVIES_TABLE.COLUMN_VOTE_AVERAGE + " REAL TEXT" +
                ")";
        db.execSQL(CREATE_TABLE_MOVIE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MOVIES_TABLE.TABLE_NAME);
        onCreate(db);

    }

}
