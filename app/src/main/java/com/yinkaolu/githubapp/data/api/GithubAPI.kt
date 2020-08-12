package com.yinkaolu.githubapp.data.api

import com.yinkaolu.githubapp.data.model.GithubRepo
import com.yinkaolu.githubapp.data.model.GithubUser
import retrofit2.Call
import retrofit2.http.GET

interface GithubAPI {
    @GET("users/{userName}")
    fun getUser(userName: String?): Call<GithubUser>
    @GET("users/{userName}/repos")
    fun getUserRepo(userName: String?): Call<List<GithubRepo>>
}