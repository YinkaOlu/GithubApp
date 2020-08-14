package com.yinkaolu.githubapp

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.yinkaolu.githubapp.data.provider.ProviderError
import com.yinkaolu.githubapp.data.provider.ProviderErrorType
import com.yinkaolu.githubapp.data.provider.DataProviderCallback
import com.yinkaolu.githubapp.data.provider.RetrofitDataProvider
import com.yinkaolu.githubapp.data.model.GithubRepos
import com.yinkaolu.githubapp.data.model.GithubUser
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.net.HttpURLConnection
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Tests to cover default Retrofit GithubDataProvider behaviour with API inputs.
 */
class RetrofitGithubDataProviderTest {
    private lateinit var server: MockWebServer
    private lateinit var client: RetrofitDataProvider

    lateinit var testUser: GithubUser

    lateinit var testRepos: GithubRepos

    private val userResponse = MockResponse()
    private val reposResponse = MockResponse()
    private val failedResponse = MockResponse()

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        client = RetrofitDataProvider.instance
        client.set(server.url("").toString())

        userResponse.setBody(GithubTestJson.getUserJsonString())
        reposResponse.setBody(GithubTestJson.getRepoJsonString())

        failedResponse.setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)

        testUser = Gson().fromJson(GithubTestJson.getUserJson(), GithubUser::class.java)
        val test = GithubTestJson.getReposJson()
        testRepos = Gson().fromJson(test, GithubRepos::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    // ======= User detail Test =======
    @Test
    fun testDataTransfer_getUserDetails_successful() {
        server.enqueue(userResponse)
        val latch = CountDownLatch(1)
        client.getUserDetails("test", object : DataProviderCallback<GithubUser> {
            override fun onSuccess(payload: GithubUser) {
                assertEquals(testUser.name, payload.name)
                assertEquals(testUser.avatarURL, payload.avatarURL)
                latch.countDown()
            }

            override fun onFailure(error: ProviderError) {
                assertTrue("User detail call should not fail", false)
                latch.countDown()
            }
        })

        latch.await(200, TimeUnit.MILLISECONDS)
    }

    @Test
    fun testDataTransfer_getUserDetails_failed() {
        server.enqueue(failedResponse)
        val latch = CountDownLatch(1)
        client.getUserDetails("test", object : DataProviderCallback<GithubUser> {
            override fun onSuccess(payload: GithubUser) {
                assertTrue("User detail call should not succeed", false)
                latch.countDown()
            }

            override fun onFailure(error: ProviderError) {
                assertEquals(ProviderErrorType.FAILED, error.type)
                latch.countDown()
            }
        })

        latch.await(200, TimeUnit.MILLISECONDS)
    }

    // ======= User Repos Test =======
    @Test
    fun testDataTransfer_getUserRepo_successful() {
        server.enqueue(reposResponse)
        val latch = CountDownLatch(1)
        client.getUserRepo("test", object : DataProviderCallback<GithubRepos> {
            override fun onSuccess(payload: GithubRepos) {
                assertEquals(1, payload.size)

                assertEquals(testRepos[0].name, payload[0].name)
                assertEquals(testRepos[0].forks, payload[0].forks)
                assertEquals(testRepos[0].stargazers, payload[0].stargazers)
                assertEquals(testRepos[0].description, payload[0].description)

                latch.countDown()
            }

            override fun onFailure(error: ProviderError) {
                assertTrue("User detail call should not fail", false)
                latch.countDown()
            }
        })

        latch.await(200, TimeUnit.MILLISECONDS)
    }

    @Test
    fun testDataTransfer_getUserRepo_failure() {
        server.enqueue(failedResponse)
        val latch = CountDownLatch(1)
        client.getUserRepo("test", object : DataProviderCallback<GithubRepos> {
            override fun onSuccess(payload: GithubRepos) {
                assertTrue("Repo call should fail", false)
                latch.countDown()
            }

            override fun onFailure(error: ProviderError) {
                assertEquals(ProviderErrorType.FAILED, error.type)
                latch.countDown()
            }
        })

        latch.await(200, TimeUnit.MILLISECONDS)
    }
}