package com.yinkaolu.githubapp.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.InputStreamReader

object GithubSampleJson {
    fun getUserJson(context: Context): JsonElement? {
        val inputStream = context.assets.open("user.json")
        return JsonParser().parse(InputStreamReader(inputStream, "UTF-8"))
    }

    fun getUserJsonString(context: Context): String {
        return Gson().toJson(getUserJson(context))
    }

    fun getReposJson(context: Context): JsonElement? {
        val inputStream = context.assets.open("repos.json")
        return JsonParser().parse(InputStreamReader(inputStream, "UTF-8"))
    }

    fun getRepoJsonString(context: Context): String {
        return Gson().toJson(getReposJson(context))
    }
}