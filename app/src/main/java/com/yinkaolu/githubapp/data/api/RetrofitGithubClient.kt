package com.yinkaolu.githubapp.data.api

import com.yinkaolu.githubapp.data.model.GithubRepo
import com.yinkaolu.githubapp.data.model.GithubRepos
import com.yinkaolu.githubapp.data.model.GithubUser
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import kotlin.concurrent.thread

class RetrofitGithubClient private constructor(url: String): GithubAPIClient {
    private lateinit var api: GithubAPI

    init {
        set(url)
    }

    fun set(url: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
        this.api = retrofit.create(GithubAPI::class.java)
    }

    override fun getUserDetails(userID: String, cb: ClientCallback<GithubUser>) {

        api.getUser(userID).enqueue(object : Callback<GithubUser> {
            override fun onFailure(call: Call<GithubUser>, t: Throwable) {
                cb.onFailure(ApiError(ApiErrorType.FAILED, "User detail call to server was unsuccessful\n\n${t.message}"))
            }

            override fun onResponse(call: Call<GithubUser>, response: Response<GithubUser>) {
                if (!response.isSuccessful)
                    return cb.onFailure(ApiError(ApiErrorType.FAILED, "Call for user detail was not successful.\n${response.message()}"))

                val user = response.body()
                    ?: return cb.onFailure(ApiError(ApiErrorType.NOT_FOUND, "Response does not have a user"))

                cb.onSuccess(user)
            }

        })

    }

    override fun getUserRepo(userID: String, cb: ClientCallback<GithubRepos>) {
        api.getUserRepo(userID).enqueue(object : Callback<GithubRepos> {
            override fun onFailure(call: Call<GithubRepos>, t: Throwable) {
                cb.onFailure(ApiError(ApiErrorType.FAILED, "User detail call to server was unsuccessful\n\n${t.message}"))
            }

            override fun onResponse(call: Call<GithubRepos>, response: Response<GithubRepos>) {
                if (!response.isSuccessful)
                    return cb.onFailure(ApiError(ApiErrorType.FAILED, "Call for user repo detail was not successful.\n${response.message()}"))

                val repositoryDetails = response.body()
                    ?: return cb.onFailure(ApiError(ApiErrorType.NOT_FOUND, "Response does not have repository details"))

                cb.onSuccess(repositoryDetails)
            }

        })
    }

    companion object {
        private const val DEFAULT_URL = "https://api.github.com/"
        var instance: RetrofitGithubClient = RetrofitGithubClient(DEFAULT_URL)
    }
}