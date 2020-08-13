package com.yinkaolu.githubapp.viewmodel

import androidx.lifecycle.LiveData
import com.yinkaolu.githubapp.data.model.GithubRepo
import com.yinkaolu.githubapp.data.model.GithubUser
import com.yinkaolu.githubapp.data.repository.DefaultGithubRepository
import com.yinkaolu.githubapp.data.repository.GithubRepository

class GithubViewModel(repository: GithubRepository = DefaultGithubRepository()) {
    private val repository: GithubRepository = repository
    private val previousUserName = ""

    val user: LiveData<GithubUser>? = repository.currentUser
    val repos: LiveData<List<GithubRepo>>? = repository.currentRepoList

    fun loadUserData(userName: String, forceDownload: Boolean=false) {
        if (userName !== previousUserName || forceDownload) {
            repository.loadUser(userName)
            repository.loadUserRepo(userName)
        }
    }
}