package com.yinkaolu.githubapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yinkaolu.githubapp.data.api.ApiError
import com.yinkaolu.githubapp.data.api.ClientCallback
import com.yinkaolu.githubapp.data.api.GithubAPIClient
import com.yinkaolu.githubapp.data.api.RetrofitGithubClient
import com.yinkaolu.githubapp.data.model.GithubRepo
import com.yinkaolu.githubapp.data.model.GithubUser

class GithubRepository(apiClient: GithubAPIClient? = null) {
    private var apiClient: GithubAPIClient = apiClient ?: RetrofitGithubClient()

    private val _currentUser: MutableLiveData<GithubUser> = MutableLiveData()
    val currentUser: LiveData<GithubUser> = _currentUser

    private val _currentRepoList: MutableLiveData<List<GithubRepo>> = MutableLiveData()
    val currentRepoList: LiveData<List<GithubRepo>> = _currentRepoList

    private val _userApiError: MutableLiveData<ApiError?> = MutableLiveData()
    val userApiError: LiveData<ApiError?> = _userApiError

    private val _repoApiError: MutableLiveData<ApiError?> = MutableLiveData()
    val repoApiError: LiveData<ApiError?> = _repoApiError


    fun loadUser(userName: String) {
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

    fun loadUserRepo(userName: String? = null) {
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