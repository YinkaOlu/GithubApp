package com.yinkaolu.githubapp.data.api

import com.yinkaolu.githubapp.data.model.GithubRepo
import com.yinkaolu.githubapp.data.model.GithubRepos
import com.yinkaolu.githubapp.data.model.GithubUser

interface GithubAPIClient {
    fun getUserDetails(userID: String, cb: ClientCallback<GithubUser>)
    fun getUserRepo(userID: String, cb: ClientCallback<GithubRepos>)
}

interface ClientCallback<T> {
    fun onSuccess(payload: T)
    fun onFailure(error: ApiError)
}