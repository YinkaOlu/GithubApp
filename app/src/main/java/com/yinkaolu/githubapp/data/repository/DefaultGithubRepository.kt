package com.yinkaolu.githubapp.data.repository

import com.yinkaolu.githubapp.data.api.ApiError
import com.yinkaolu.githubapp.data.api.ClientCallback
import com.yinkaolu.githubapp.data.api.GithubAPIClient
import com.yinkaolu.githubapp.data.api.RetrofitGithubClient
import com.yinkaolu.githubapp.data.model.GithubRepo
import com.yinkaolu.githubapp.data.model.GithubUser

class DefaultGithubRepository(apiClient: GithubAPIClient = RetrofitGithubClient()): GithubRepository(apiClient) {
    override fun loadUser(userName: String) {
        apiClient.getUserDetails(userName, object : ClientCallback<GithubUser> {
            override fun onSuccess(payload: GithubUser) {
                _currentUser.value = payload
                _userApiError.value = null
            }

            override fun onFailure(error: ApiError) {
                _userApiError.value = error
            }

        })
    }

    override fun loadUserRepo(userName: String?) {
        val githubUserName = userName ?: _currentUser.value?.name
        if (githubUserName !== null) {
            apiClient.getUserRepo(githubUserName, object : ClientCallback<List<GithubRepo>> {
                override fun onSuccess(payload: List<GithubRepo>) {
                    _currentRepoList.value = payload
                    _repoApiError.value = null
                }

                override fun onFailure(error: ApiError) {
                    _repoApiError.value = error
                }

            })
        } else {
            throw IllegalArgumentException("user name must be provided if there is no currently loaded Github user data")
        }
    }
}