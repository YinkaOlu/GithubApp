package com.yinkaolu.githubapp.data.api

import com.yinkaolu.githubapp.data.model.GithubRepo
import com.yinkaolu.githubapp.data.model.GithubUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubAPI {
    @GET("users/{userName}")
    fun getUser(@Path("userName") userName: String?): Call<GithubUser>
    @GET("users/{userName}/repos")
    fun getUserRepo(@Path("userName") userName: String?): Call<List<GithubRepo>>
}