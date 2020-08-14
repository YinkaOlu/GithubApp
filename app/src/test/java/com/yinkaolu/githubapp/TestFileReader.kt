package com.yinkaolu.githubapp

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.InputStreamReader

object GithubTestJson {
    fun getUserJson(): JsonElement? {
        val inputStream = this.javaClass.classLoader?.getResourceAsStream("user.json")
        return JsonParser().parse(InputStreamReader(inputStream, "UTF-8"))
    }

    fun getUserJsonString(): String {
        return Gson().toJson(getUserJson())
    }

    fun getReposJson(): JsonElement? {
        val inputStream = this.javaClass.classLoader?.getResourceAsStream("repos.json")
        return JsonParser().parse(InputStreamReader(inputStream, "UTF-8"))
    }

    fun getRepoJsonString(): String {
        return Gson().toJson(getReposJson())
    }
}