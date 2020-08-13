package com.yinkaolu.githubapp.data.repository

import androidx.lifecycle.LiveData
import com.yinkaolu.githubapp.data.api.ApiError
import com.yinkaolu.githubapp.data.api.ClientCallback
import com.yinkaolu.githubapp.data.api.GithubAPIClient
import com.yinkaolu.githubapp.data.api.RetrofitGithubClient
import com.yinkaolu.githubapp.data.model.GithubRepo
import com.yinkaolu.githubapp.data.model.GithubUser
import kotlin.concurrent.thread

class DefaultGithubRepository(apiClient: GithubAPIClient = RetrofitGithubClient()): GithubRepository(apiClient) {
    private val userCache: HashMap<String, GithubUser> = hashMapOf()
    private val repoCache: HashMap<String, List<GithubRepo>> = hashMapOf()

    override fun loadUser(userName: String, considerCache: Boolean): LiveData<GithubUser?> {
        if (considerCache && userCache[userName] !== null) {
            _currentUser.postValue(userCache[userName])
        } else {
            _state.postValue(DataState.LOADING)
            apiClient.getUserDetails(userName, object : ClientCallback<GithubUser> {
                override fun onSuccess(payload: GithubUser) {
                    _currentUser.postValue(payload)
                    _userApiError.postValue(null)
                    userCache[payload.name] = payload
                    _state.postValue(DataState.LOADED)
                }

                override fun onFailure(error: ApiError) {
                    _userApiError.postValue(error)
                    _state.postValue(DataState.FAILED)
                }

            })
        }
        return currentUser
    }

    override fun loadUserRepo(userName: String?, considerCache: Boolean): LiveData<List<GithubRepo>?> {
        val githubUserName = userName ?: _currentUser.value?.name
        ?: throw IllegalArgumentException("user name must be provided if there is no currently loaded Github user data")

        if (considerCache && repoCache[githubUserName] !== null) {
            _currentRepoList.postValue(repoCache[githubUserName])
        } else {
            _state.postValue(DataState.LOADING)
            apiClient.getUserRepo(githubUserName, object : ClientCallback<List<GithubRepo>> {
                override fun onSuccess(payload: List<GithubRepo>) {
                    _currentRepoList.postValue(payload)
                    _repoApiError.postValue(null)
                    repoCache[githubUserName] = payload
                    _state.postValue(DataState.LOADED)
                }

                override fun onFailure(error: ApiError) {
                    _repoApiError.postValue(error)
                    _state.postValue(DataState.FAILED)
                }

            })
        }
        return currentRepoList
    }

    override fun clear(userName: String?) {
        _currentUser.postValue(null)
        _currentRepoList.postValue(null)

        if (!userName.isNullOrEmpty()) {
            userCache.remove(userName)
            repoCache.remove(userName)
        } else {
            userCache.clear()
            repoCache.clear()
        }
        _state.postValue(DataState.EMPTY)
    }
}