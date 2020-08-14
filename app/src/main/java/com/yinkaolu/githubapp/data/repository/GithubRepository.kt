package com.yinkaolu.githubapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yinkaolu.githubapp.data.api.ApiError
import com.yinkaolu.githubapp.data.api.ClientCallback
import com.yinkaolu.githubapp.data.api.GithubAPIClient
import com.yinkaolu.githubapp.data.api.RetrofitGithubClient
import com.yinkaolu.githubapp.data.model.GithubRepo
import com.yinkaolu.githubapp.data.model.GithubUser

enum class DataState {
    EMPTY, LOADED, LOADING, FAILED
}

abstract class GithubRepository(apiClient: GithubAPIClient) {
    internal var apiClient: GithubAPIClient = apiClient

    protected val _currentUser: MutableLiveData<GithubUser?> = MutableLiveData()
    val currentUser: LiveData<GithubUser?> = _currentUser

    protected val _currentRepoList: MutableLiveData<ArrayList<GithubRepo>?> = MutableLiveData()
    val currentRepoList: LiveData<ArrayList<GithubRepo>?> = _currentRepoList

    protected val _userApiError: MutableLiveData<ApiError?> = MutableLiveData()
    val userApiError: LiveData<ApiError?> = _userApiError

    protected val _repoApiError: MutableLiveData<ApiError?> = MutableLiveData()
    val repoApiError: LiveData<ApiError?> = _repoApiError

    protected val _state: MutableLiveData<DataState> = MutableLiveData()
    val state: LiveData<DataState> = _state


    abstract fun loadUser(userName: String, considerCache: Boolean=true): LiveData<GithubUser?>
    abstract fun loadUserRepo(userName: String? = null, considerCache: Boolean=true): LiveData<ArrayList<GithubRepo>?>
    abstract fun clear(userName: String?=null)
}