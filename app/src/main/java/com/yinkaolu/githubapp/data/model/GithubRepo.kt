package com.yinkaolu.githubapp.data.model

import com.google.gson.annotations.SerializedName

class GithubRepo(
    val name: String,
    val description: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("stargazers_count")
    val stargazers: Int,
    val forks: Int
)