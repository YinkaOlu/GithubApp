package com.yinkaolu.githubapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yinkaolu.githubapp.data.api.ApiError
import com.yinkaolu.githubapp.data.api.ClientCallback
import com.yinkaolu.githubapp.data.api.GithubAPIClient
import com.yinkaolu.githubapp.data.api.RetrofitGithubClient
import com.yinkaolu.githubapp.data.model.GithubRepo
import com.yinkaolu.githubapp.data.model.GithubUser

abstract class GithubRepository(apiClient: GithubAPIClient) {
    internal var apiClient: GithubAPIClient = apiClient

    internal val _currentUser: MutableLiveData<GithubUser> = MutableLiveData()
    val currentUser: LiveData<GithubUser> = _currentUser

    internal val _currentRepoList: MutableLiveData<List<GithubRepo>> = MutableLiveData()
    val currentRepoList: LiveData<List<GithubRepo>> = _currentRepoList

    internal val _userApiError: MutableLiveData<ApiError?> = MutableLiveData()
    val userApiError: LiveData<ApiError?> = _userApiError

    internal val _repoApiError: MutableLiveData<ApiError?> = MutableLiveData()
    val repoApiError: LiveData<ApiError?> = _repoApiError


    abstract fun loadUser(userName: String)

    abstract fun loadUserRepo(userName: String? = null)
}