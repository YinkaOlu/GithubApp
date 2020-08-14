package com.yinkaolu.githubapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yinkaolu.githubapp.data.provider.ProviderError
import com.yinkaolu.githubapp.data.provider.GithubDataProvider
import com.yinkaolu.githubapp.data.model.GithubRepos
import com.yinkaolu.githubapp.data.model.GithubUser

enum class DataState {
    EMPTY, LOADED, LOADING, FAILED
}

/**
 * Abstract class defining the properties and default behaviours of a GithubRepository
 */
abstract class GithubRepository(internal var apiClient: GithubDataProvider) {

    protected val loadedUser: MutableLiveData<GithubUser?> = MutableLiveData()
    protected val loadedRepositories: MutableLiveData<GithubRepos?> = MutableLiveData()
    protected val userApiCallError: MutableLiveData<ProviderError?> = MutableLiveData()
    protected val repositoryApiCallError: MutableLiveData<ProviderError?> = MutableLiveData()
    protected val state: MutableLiveData<DataState> = MutableLiveData()

    /**
     * Live Github User data for the currently loaded user information
     */
    val currentUser: LiveData<GithubUser?> = loadedUser

    /**
     * Live Github Repository data for the currently loaded user information
     */
    val currentRepoList: LiveData<GithubRepos?> = loadedRepositories

    /**
     * Live Provider error representing error generated while getting Github User data
     */
    val userError: LiveData<ProviderError?> = userApiCallError

    /**
     * Live Provider error representing error generated while getting Github Repository data
     */
    val repositoryError: LiveData<ProviderError?> = repositoryApiCallError

    /**
     * Live State of data loading state in repository
     */
    val repositoryDataState: LiveData<DataState> = state

    /**
     * Call to load Github User data from source
     */
    abstract fun loadUser(userName: String, considerCache: Boolean=true): LiveData<GithubUser?>

    /**
     * Call to load Github Repository data from source
     */
    abstract fun loadUserRepo(userName: String? = null, considerCache: Boolean=true): LiveData<GithubRepos?>

    /**
     * Call to clear all or specific data from repository
     */
    abstract fun clear(userName: String?=null)
}