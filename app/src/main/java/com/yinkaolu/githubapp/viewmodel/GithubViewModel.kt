package com.yinkaolu.githubapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yinkaolu.githubapp.data.provider.ProviderError
import com.yinkaolu.githubapp.data.model.GithubUser
import com.yinkaolu.githubapp.data.repository.DataState
import com.yinkaolu.githubapp.data.repository.DefaultGithubRepository
import com.yinkaolu.githubapp.data.repository.GithubRepository

class GithubViewModel(
    private val repository: GithubRepository = DefaultGithubRepository()
): ViewModel() {

    val user: LiveData<GithubUser?>? = repository.currentUser
    val repos = repository.currentRepoList

    val userApiError: LiveData<ProviderError?> = repository.userError
    val repoApiError: LiveData<ProviderError?> = repository.repositoryError
    val state: LiveData<DataState> = repository.repositoryDataState

    private val searchHistory: ArrayList<String> = arrayListOf()

    fun getUser(inputUserName: String, freshDownload: Boolean=false) {
        /**
         * Load user data if,
         * 1. input name is not empty
         * and
         * 2. is a fresh download or not the currently presented user
         */
        if (inputUserName.isNotEmpty()) {
            repository.loadUser(inputUserName, !freshDownload)
            repository.loadUserRepo(inputUserName, !freshDownload)
            searchHistory.add(inputUserName)
        }
    }
}