package com.example.ngithubuser.getData

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.example.ngithubuser.getData.DatabaseContract.FavColumns.Companion.ID
import com.example.ngithubuser.getData.DatabaseContract.FavColumns.Companion.LOGIN
import com.example.ngithubuser.getData.DatabaseContract.FavColumns.Companion.TABLE_NAME

class FavHelper(context: Context) {
    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME


        private var INSTANCE: FavHelper? = null
        fun getInstance(context: Context): FavHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$ID ASC"
        )
    }

    fun queryById(username: String?): Cursor {
        return database.rawQuery(
            "SELECT $LOGIN FROM $DATABASE_TABLE WHERE $LOGIN = '$username'",
            null
        )
    }

    fun insert(values: ContentValues): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteById(username: String?): Int {
        return database.delete(DATABASE_TABLE, "$LOGIN = '$username'", null)
    }

}