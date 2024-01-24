package com.itskidan.kinostock.data

import android.content.ContentValues
import android.database.Cursor
import com.itskidan.kinostock.data.database.DatabaseHelper
import com.itskidan.kinostock.domain.Film
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(databaseHelper: DatabaseHelper) {
    val filmsDataBase = ArrayList<Film>()
    // Initialize the object to interact with the database
    private val sqlDb = databaseHelper.readableDatabase
    // Create a cursor to process requests from the database
    private lateinit var cursor: Cursor
    fun putToDb(film: Film) {
        // Create an object that will store key-value pairs in order to
        // to put the required data in the required columns
        val cv = ContentValues()
        cv.apply {
            put(DatabaseHelper.COLUMN_ID, film.id)
            put(DatabaseHelper.COLUMN_TITLE, film.title)
            put(DatabaseHelper.COLUMN_POSTER, film.poster)
            put(DatabaseHelper.COLUMN_DESCRIPTION, film.description)
            put(DatabaseHelper.COLUMN_RATING, film.rating)
            put(DatabaseHelper.COLUMN_RELEASE_DATE, film.releaseDate)
        }
        //Put the film into the database
        sqlDb.insert(DatabaseHelper.TABLE_NAME, null, cv)
    }
    fun getAllFromDB(): ArrayList<Film>{
        // Create a cursor based on the request "Get everything from the table"
        cursor = sqlDb.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_NAME}",null)
        // We will save the result of receiving data here
        val result = mutableListOf<Film>()
        // Check if there is at least one line in the response to the request
        if (cursor.moveToFirst()) {
            // Iterate through the table as long as there are records, and create a Film object based on it
            do {
                val id = cursor.getInt(1)
                val title = cursor.getString(2)
                val poster = cursor.getString(3)
                val description = cursor.getString(4)
                val rating = cursor.getDouble(5)
                val releaseDate = cursor.getString(6)
                result.add(Film(
                    id = id,
                    title = title,
                    poster = poster,
                    releaseDate = releaseDate,
                    description = description,
                    rating = rating,
                    isInFavorites = false
                ))
            } while (cursor.moveToNext())
        }
        //Return the list of films
        return ArrayList(result)
    }
}