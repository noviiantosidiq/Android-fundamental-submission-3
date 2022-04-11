package com.example.ngithubuser.getData

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.ngithubuser.getData.DatabaseContract.FavColumns.Companion.TABLE_NAME

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "GithubUsers"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                "(${DatabaseContract.FavColumns.ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${DatabaseContract.FavColumns.LOGIN} TEXT NOT NULL," +
                "${DatabaseContract.FavColumns.NAME} TEXT NOT NULL," +
                "${DatabaseContract.FavColumns.COMPANY} TEXT NOT NULL," +
                "${DatabaseContract.FavColumns.LOCATION} TEXT NOT NULL," +
                "${DatabaseContract.FavColumns.REPOSITORY} INTEGER NOT NULL," +
                "${DatabaseContract.FavColumns.FOLLOWERS} INTEGER NOT NULL," +
                "${DatabaseContract.FavColumns.FOLLOWING} INTEGER NOT NULL," +
                "${DatabaseContract.FavColumns.DATE} TEXT NOT NULL," +
                "${DatabaseContract.FavColumns.AVATAR} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}