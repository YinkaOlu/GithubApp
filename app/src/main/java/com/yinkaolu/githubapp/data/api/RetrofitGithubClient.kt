package com.yinkaolu.githubapp.data.api

import com.yinkaolu.githubapp.data.model.GithubRepo
import com.yinkaolu.githubapp.data.model.GithubUser
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import kotlin.concurrent.thread

class RetrofitGithubClient(api: GithubAPI? = null): GithubAPIClient {
    private var api: GithubAPI

    init {
        if (api !== null) {
            this.api = api
        } else {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder().build())
                .build()
            this.api = retrofit.create(GithubAPI::class.java)
        }
    }

    override fun getUserDetails(userName: String, cb: ClientCallback<GithubUser>) {
        thread {
            try {
                val response = api.getUser(userName).execute()

                if (!response.isSuccessful)
                    return@thread cb.onFailure(ApiError(ApiErrorType.FAILED, "Call for user detail was not successful"))

                val user = response.body()
                    ?: return@thread cb.onFailure(ApiError(ApiErrorType.NOT_FOUND, "Response does not have a user"))

                cb.onSuccess(user)
            } catch (ioe: IOException) {
                cb.onFailure(ApiError(ApiErrorType.FAILED, "User detail call to server was unsuccessful"))
            } catch (rte: RuntimeException) {
                cb.onFailure(ApiError(ApiErrorType.FAILED, "Error occurred while trying to encode request or decode response"))
            }
        }

    }

    override fun getUserRepo(userName: String, cb: ClientCallback<ArrayList<GithubRepo>>) {
        thread {
            try {
                val response = api.getUserRepo(userName).execute()
                if (!response.isSuccessful)
                    return@thread cb.onFailure(ApiError(ApiErrorType.FAILED, "Call for user repo detail was not successful"))

                val repositoryDetails = response.body()
                    ?: return@thread cb.onFailure(ApiError(ApiErrorType.NOT_FOUND, "Response does not have repository details"))

                cb.onSuccess(repositoryDetails)
            } catch (ioe: IOException) {
                cb.onFailure(ApiError(ApiErrorType.FAILED, "User detail call to server was unsuccessful"))
            } catch (rte: RuntimeException) {
                cb.onFailure(ApiError(ApiErrorType.FAILED, "Error occurred while trying to encode request or decode response"))
            }
        }
    }
}