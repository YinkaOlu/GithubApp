package com.yinkaolu.githubapp.data.model

import com.google.gson.annotations.SerializedName

class GithubUser(
    val name: String,
    @SerializedName("avatar_url")
    val avatarURL: String
)