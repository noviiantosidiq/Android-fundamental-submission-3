package com.example.ngithubuser.getData

import android.provider.BaseColumns

class DatabaseContract {
    internal class FavColumns : BaseColumns{
        companion object{
            const val  TABLE_NAME = "Favorite"
            const val ID = "id"
            const val LOGIN =  "username"
            const val NAME = "nama"
            const val LOCATION = "location"
            const val COMPANY = "company"
            const val REPOSITORY = "repository"
            const val FOLLOWING = "following"
            const val FOLLOWERS = "followers"
            const val AVATAR = "avatar"
            const val DATE = "date"
        }
    }

}