package com.example.ngithubuser.getData

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Fav(
    var usename: String,
    var nama: String,
    var location: String,
    var company: String,
    var repository: String,
    var followers: String,
    var following: String,
    var avatar: String,
) : Parcelable
