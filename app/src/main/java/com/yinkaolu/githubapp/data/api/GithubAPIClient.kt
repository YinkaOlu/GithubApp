package com.yinkaolu.githubapp.data.api

import com.yinkaolu.githubapp.data.model.GithubRepo
import com.yinkaolu.githubapp.data.model.GithubUser

interface GithubAPIClient {
    fun getUserDetails(userName: String, cb: ClientCallback<GithubUser>)
    fun getUserRepo(userName: String, cb: ClientCallback<ArrayList<GithubRepo>>)
}

interface ClientCallback<T> {
    fun onSuccess(payload: T)
    fun onFailure(error: ApiError)
}