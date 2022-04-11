package com.example.ngithubuser.getData

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataUser(
    var avatar: Int,
    var name: String,
    var followers: String,
    var repository: String,
    var location: String,
    var company: String,
    var usename: String,
    var following: String
) : Parcelable
