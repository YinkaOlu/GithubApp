package com.yinkaolu.githubapp

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import com.yinkaolu.githubapp.data.GithubSampleJson
import com.yinkaolu.githubapp.data.model.GithubRepos
import com.yinkaolu.githubapp.data.model.GithubUser
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*


/**
 * Unit tests to check mappings from expected json data to Models
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class GithubModelTest {
    lateinit var ctx: Context

    @Before
    fun setup() {
        ctx = ApplicationProvider.getApplicationContext()
    }
    @Test
    fun testConversation_GithubUser() {
        val githubUser: GithubUser = Gson().fromJson(GithubSampleJson.getUserJson(ctx), GithubUser::class.java)

        assertEquals("https://avatars3.githubusercontent.com/u/583231?v=4", githubUser.avatarURL)
        assertEquals("The Octocat", githubUser.name)
    }

    @Test
    fun testConversation_GithubRepos() {
        val githubRepos: GithubRepos = Gson().fromJson(GithubSampleJson.getReposJson(ctx), GithubRepos::class.java)
        val githubRepo = githubRepos[0]

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