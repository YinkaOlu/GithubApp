package com.yinkaolu.githubapp.data.repository

import com.yinkaolu.githubapp.data.api.ApiError
import com.yinkaolu.githubapp.data.api.ClientCallback
import com.yinkaolu.githubapp.data.api.GithubAPIClient
import com.yinkaolu.githubapp.data.api.RetrofitGithubClient
import com.yinkaolu.githubapp.data.model.GithubRepo
import com.yinkaolu.githubapp.data.model.GithubUser
import kotlin.concurrent.thread

class DefaultGithubRepository(apiClient: GithubAPIClient = RetrofitGithubClient()): GithubRepository(apiClient) {
    override fun loadUser(userName: String) {
        apiClient.getUserDetails(userName, object : ClientCallback<GithubUser> {
            override fun onSuccess(payload: GithubUser) {
                _currentUser.postValue(payload)
                _userApiError.postValue(null)
            }

            override fun onFailure(error: ApiError) {
                _userApiError.postValue(error)
            }

        })
    }

    override fun loadUserRepo(userName: String?) {
        val githubUserName = userName ?: _currentUser.value?.name
        if (githubUserName !== null) {
            apiClient.getUserRepo(githubUserName, object : ClientCallback<List<GithubRepo>> {
                override fun onSuccess(payload: List<GithubRepo>) {
                    _currentRepoList.postValue(payload)
                    _repoApiError.postValue(null)
                }

                override fun onFailure(error: ApiError) {
                    _repoApiError.postValue(error)
                }

            })
        } else {
            throw IllegalArgumentException("user name must be provided if there is no currently loaded Github user data")
        }
    }
}