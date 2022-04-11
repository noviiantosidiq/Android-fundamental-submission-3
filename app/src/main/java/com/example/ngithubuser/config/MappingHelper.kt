package com.example.ngithubuser.config

import android.database.Cursor
import com.example.ngithubuser.getData.Fav
import com.example.ngithubuser.getData.DatabaseContract

object MappingHelper {

    fun mapCursorToArrayList(FavCursor: Cursor?): ArrayList<Fav> {
        val favList = ArrayList<Fav>()

        FavCursor?.apply {
            while (moveToNext()) {
                val user = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.LOGIN))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.NAME))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.LOCATION))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.COMPANY))
                val repo = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.REPOSITORY))
                val followers = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.FOLLOWERS))
                val following = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.FOLLOWING))
                val ava = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.AVATAR))
                favList.add(Fav(user,name,location,company,repo,followers,following,ava))
            }
        }
        return favList
    }
}