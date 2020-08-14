package com.yinkaolu.githubapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yinkaolu.githubapp.data.provider.ProviderError
import com.yinkaolu.githubapp.data.model.GithubUser
import com.yinkaolu.githubapp.data.repository.DataState
import com.yinkaolu.githubapp.data.repository.DefaultGithubRepository
import com.yinkaolu.githubapp.data.repository.GithubRepository

/**
 * View model to control the data flow between view and data layer.
 */
class GithubViewModel(
    private val repository: GithubRepository = DefaultGithubRepository()
): ViewModel() {

    val user: LiveData<GithubUser?>? = repository.currentUser
    val repos = repository.currentRepoList

    val userApiError: LiveData<ProviderError?> = repository.userError
    val repoApiError: LiveData<ProviderError?> = repository.repositoryError
    val state: LiveData<DataState> = repository.repositoryDataState

    /**
     * Call to load data user information and repository information for user from repository
     * Loads user data if input name is not empty
     * Does not consider cache if doing a fresh download (ie, when refreshing stale data
     */
    fun getUser(inputUserName: String, freshDownload: Boolean=false) {
        if (inputUserName.isNotEmpty()) {
            repository.loadUser(inputUserName, !freshDownload)
            repository.loadUserRepo(inputUserName, !freshDownload)
        }
    }
}