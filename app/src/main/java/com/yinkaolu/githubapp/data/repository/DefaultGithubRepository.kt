package com.yinkaolu.githubapp.data.repository

import androidx.lifecycle.LiveData
import com.yinkaolu.githubapp.data.provider.ProviderError
import com.yinkaolu.githubapp.data.provider.DataProviderCallback
import com.yinkaolu.githubapp.data.provider.GithubDataProvider
import com.yinkaolu.githubapp.data.provider.RetrofitDataProvider
import com.yinkaolu.githubapp.data.model.GithubRepos
import com.yinkaolu.githubapp.data.model.GithubUser

/**
 * Default implementation of GithubRepository
 */
class DefaultGithubRepository(apiClient: GithubDataProvider = RetrofitDataProvider.instance): GithubRepository(apiClient) {
    private val userCache: HashMap<String, GithubUser> = hashMapOf()
    private val repoCache: HashMap<String, GithubRepos> = hashMapOf()

    override fun loadUser(userName: String, considerCache: Boolean): LiveData<GithubUser?> {
        if (considerCache && userCache[userName] !== null) {
            loadedUser.postValue(userCache[userName])
        } else {
            state.postValue(DataState.LOADING)
            apiClient.getUserDetails(userName, object : DataProviderCallback<GithubUser> {
                override fun onSuccess(payload: GithubUser) {
                    loadedUser.postValue(payload)
                    userApiCallError.postValue(null)
                    userCache[payload.name] = payload
                    state.postValue(DataState.LOADED)
                }

                override fun onFailure(error: ProviderError) {
                    userApiCallError.postValue(error)
                    state.postValue(DataState.FAILED)
                }

            })
        }
        return currentUser
    }

    override fun loadUserRepo(userName: String?, considerCache: Boolean): LiveData<GithubRepos?> {
        val githubUserName = userName ?: loadedUser.value?.name
        ?: throw IllegalArgumentException("user name must be provided if there is no currently loaded Github user data")

        if (considerCache && repoCache[githubUserName] !== null) {
            loadedRepositories.postValue(repoCache[githubUserName])
        } else {
            state.postValue(DataState.LOADING)
            apiClient.getUserRepo(githubUserName, object : DataProviderCallback<GithubRepos> {
                override fun onSuccess(payload: GithubRepos) {
                    loadedRepositories.postValue(payload)
                    repositoryApiCallError.postValue(null)
                    repoCache[githubUserName] = payload
                    state.postValue(DataState.LOADED)
                }

                override fun onFailure(error: ProviderError) {
                    repositoryApiCallError.postValue(error)
                    state.postValue(DataState.FAILED)
                }

            })
        }
        return currentRepoList
    }

    override fun clear(userName: String?) {
        loadedUser.postValue(null)
        loadedRepositories.postValue(null)

        if (!userName.isNullOrEmpty()) {
            userCache.remove(userName)
            repoCache.remove(userName)
        } else {
            userCache.clear()
            repoCache.clear()
        }
        state.postValue(DataState.EMPTY)
    }
}