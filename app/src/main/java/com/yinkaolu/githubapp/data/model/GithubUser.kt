package com.yinkaolu.githubapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * Class representing User data from Github API
 */
class GithubUser(
    val name: String,
    @SerializedName("avatar_url")
    val avatarURL: String
)