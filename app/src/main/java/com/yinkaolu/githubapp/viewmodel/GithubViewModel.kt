package com.yinkaolu.githubapp.viewmodel

import androidx.lifecycle.LiveData
import com.yinkaolu.githubapp.data.api.ApiError
import com.yinkaolu.githubapp.data.model.GithubRepo
import com.yinkaolu.githubapp.data.model.GithubUser
import com.yinkaolu.githubapp.data.repository.DefaultGithubRepository
import com.yinkaolu.githubapp.data.repository.GithubRepository
import kotlin.concurrent.thread

class GithubViewModel(repository: GithubRepository = DefaultGithubRepository()) {
    private val repository: GithubRepository = repository
    private var previousUserName = ""

    val user: LiveData<GithubUser>? = repository.currentUser
    val userApiError: LiveData<ApiError?> = repository.userApiError

    val repos: LiveData<List<GithubRepo>>? = repository.currentRepoList
    val repoApiError: LiveData<ApiError?> = repository.repoApiError

    fun loadUserData(userName: String, forceDownload: Boolean=false) {
        if ((!userName.isNullOrEmpty() && userName !== previousUserName) || forceDownload) {
            repository.loadUser(userName)
            repository.loadUserRepo(userName)

            previousUserName = userName
        }
    }
}