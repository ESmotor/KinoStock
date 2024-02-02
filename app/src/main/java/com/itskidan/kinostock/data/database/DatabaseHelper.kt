package com.itskidan.kinostock.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import javax.inject.Inject

class DatabaseHelper @Inject constructor(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    // Create a table for movies method
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_NUMBER INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_ID INTEGER UNIQUE," +
                    "$COLUMN_TITLE TEXT UNIQUE," +
                    "$COLUMN_POSTER TEXT," +
                    "$COLUMN_DESCRIPTION TEXT," +
                    "$COLUMN_RELEASE_DATE TEXT," +
                    "$COLUMN_RATING REAL," +
                    "$COLUMN_FAVORITE INTEGER);"
        )
        db?.execSQL(
            "CREATE TABLE $TABLE_FAVORITES_NAME (" +
                    "$COLUMN_NUMBER INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_ID INTEGER UNIQUE," +
                    "$COLUMN_TITLE TEXT UNIQUE," +
                    "$COLUMN_POSTER TEXT," +
                    "$COLUMN_DESCRIPTION TEXT," +
                    "$COLUMN_RELEASE_DATE TEXT," +
                    "$COLUMN_RATING REAL," +
                    "$COLUMN_FAVORITE INTEGER);"
        )

    }

    // We don't expect migrations, so the method is empty
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

companion object{
    // name of our DB
    private const val DATABASE_NAME = "films.db"
    // version of our DB
    private const val DATABASE_VERSION = 1
    // Constants for working with the table, we will need them in CRUD operations and,
    // possibly in composing queries
    const val TABLE_NAME = "films_table"
    const val TABLE_FAVORITES_NAME = "favorites_films_table"
    const val COLUMN_NUMBER = "no"
    const val COLUMN_ID = "id"
    const val COLUMN_TITLE = "title"
    const val COLUMN_POSTER = "poster_path"
    const val COLUMN_DESCRIPTION = "overview"
    const val COLUMN_RATING = "vote_average"
    const val COLUMN_RELEASE_DATE = "date_of_release"
    const val COLUMN_FAVORITE = "is_favorite"

}

}