package com.example.ngithubuser.services

import com.example.ngithubuser.BuildConfig
import com.example.ngithubuser.getData.*
import retrofit2.Call
import retrofit2.http.*

const val token = BuildConfig.GithubAPIKEY

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token $token")
    fun getSearchResult(
        @Query("q") query: String
    ): Call<SearchRespone>

    @GET("users/{username}")
    @Headers("Authorization: token $token")
    fun getDetail(
        @Path("username") query: String
    ): Call<DetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token $token")
    fun getFollowers(
        @Path("username") query: String
    ): Call<List<FollowResponseItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token $token")
    fun getFollowing(
        @Path("username") query: String
    ): Call<List<FollowResponseItem>>
}