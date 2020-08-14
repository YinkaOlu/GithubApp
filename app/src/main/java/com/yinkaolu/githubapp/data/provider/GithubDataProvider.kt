package com.yinkaolu.githubapp.data.provider

import com.yinkaolu.githubapp.data.model.GithubRepos
import com.yinkaolu.githubapp.data.model.GithubUser

/**
 * Interface that defines that behaviour of Github Data Provider.
 * Implementation will retrieve data from Github source
 */
interface GithubDataProvider {

    /**
     * Retrieves public user data for the Github user associated with the provided userID
     * @param userID user id of Github User
     * @param cb DataProviderCallback that will return either the user data or an failure
     */
    fun getUserDetails(userID: String, cb: DataProviderCallback<GithubUser>)

    /**
     * Retrieves public Github repository data for user associated with the provided userID
     * @param userID user id of Github User
     * @param cb DataProviderCallback that will return either the repository data or an failure
     */
    fun getUserRepo(userID: String, cb: DataProviderCallback<GithubRepos>)
}

interface DataProviderCallback<T> {
    fun onSuccess(payload: T)
    fun onFailure(error: ProviderError)
}