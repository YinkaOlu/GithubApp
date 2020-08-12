package com.yinkaolu.githubapp

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.yinkaolu.githubapp.data.model.GithubRepo
import com.yinkaolu.githubapp.data.model.GithubUser
import org.junit.Test
import org.junit.Assert.*


class GithubModelTest {
    @Test
    fun testUserModelConversion() {
        val userJsonElement = JsonParser().parse("{\"name\": \"The Octocat\",\"avatar_url\": \"https://avatars3.githubusercontent.com/u/583231?v=4\"}")
        val githubUser: GithubUser = Gson().fromJson(userJsonElement, GithubUser::class.java)

        assertEquals(githubUser.avatarURL, "https://avatars3.githubusercontent.com/u/583231?v=4")
        assertEquals(githubUser.name, "The Octocat")
    }

    @Test
    fun testRepoModelConversion() {
        val repoJsonElement = JsonParser().parse("{\n" +
                "        \"name\" : \"Hello-World\",\n" +
                "        \"description\" : \"My first repository on GitHub!\",\n" +
                "        \"updated_at\" : \"2017-08-14T08:08:10Z\",\n" +
                "        \"stargazers_count\": 1421,\n" +
                "        \"forks\" : 1176\n" +
                "    }")
        val githubUser: GithubRepo = Gson().fromJson(repoJsonElement, GithubRepo::class.java)

        assertEquals( "Hello-World", githubUser.name)
        assertEquals( "My first repository on GitHub!", githubUser.description)
        assertEquals( "2017-08-14T08:08:10Z", githubUser.updatedAt)
        assertEquals(1421, githubUser.stargazers)
        assertEquals(1176, githubUser.forks)
    }
}