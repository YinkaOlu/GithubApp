package com.yinkaolu.githubapp

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.yinkaolu.githubapp.data.model.GithubRepo
import com.yinkaolu.githubapp.data.model.GithubUser
import org.junit.Test
import org.junit.Assert.*
import java.util.*

/**
 * Unit tests to check mappings from expected json data to Models
 */
class GithubModelTest {
    @Test
    fun testConversation_GithubUser() {
        val userJsonElement = JsonParser().parse(GithubTestJson.testUserJsonString)
        val githubUser: GithubUser = Gson().fromJson(userJsonElement, GithubUser::class.java)

        assertEquals("https://avatars3.githubusercontent.com/u/583231?v=4", githubUser.avatarURL)
        assertEquals("The Octocat", githubUser.name)
    }

    @Test
    fun testConversation_GithubRepo() {
        val repoJsonElement = JsonParser().parse(GithubTestJson.testRepoJsonString)
        val githubRepo: GithubRepo = Gson().fromJson(repoJsonElement, GithubRepo::class.java)

        assertEquals( "Hello-World", githubRepo.name)
        assertEquals( "My first repository on GitHub!", githubRepo.description)
        assertEquals( "2017-08-14T08:08:10Z", githubRepo.updatedAt)
        assertEquals(1421, githubRepo.stargazers)
        assertEquals(1176, githubRepo.forks)

        val cal = Calendar.getInstance()
        cal.time = githubRepo.updatedDate

        assertEquals(2017, cal.get(Calendar.YEAR))
        assertEquals(7, cal.get(Calendar.MONTH))
        assertEquals(14, cal.get(Calendar.DAY_OF_MONTH))
    }
}