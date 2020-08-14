package com.yinkaolu.githubapp

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.yinkaolu.githubapp.data.api.ApiError
import com.yinkaolu.githubapp.data.api.ApiErrorType
import com.yinkaolu.githubapp.data.api.ClientCallback
import com.yinkaolu.githubapp.data.api.RetrofitGithubClient
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

class RetrofitGithubClientTest {
    lateinit var server: MockWebServer
    lateinit var client: RetrofitGithubClient

    val userJsonElement = JsonParser().parse(GithubTestJson.testUserJsonString)
    val testUser: GithubUser = Gson().fromJson(userJsonElement, GithubUser::class.java)

    val reposJsonElement = JsonParser().parse(GithubTestJson.testReposJsonString)
    val testRepos: GithubRepos = Gson().fromJson(reposJsonElement, GithubRepos::class.java)

    val userResponse = MockResponse()
    val reposResponse = MockResponse()
    val failedResponse = MockResponse()

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        client = RetrofitGithubClient.instance
        client.set(server.url("").toString())

        userResponse.setBody(GithubTestJson.testUserJsonString)
        reposResponse.setBody(GithubTestJson.testReposJsonString)

        failedResponse.setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    // ======= User detail Test =======
    @Test
    fun testUserDetailAccuracy() {
        server.enqueue(userResponse)
        val latch = CountDownLatch(1)
        client.getUserDetails("test", object : ClientCallback<GithubUser> {
            override fun onSuccess(payload: GithubUser) {
                assertEquals(testUser.name, payload.name)
                assertEquals(testUser.avatarURL, payload.avatarURL)
                latch.countDown()
            }

            override fun onFailure(error: ApiError) {
                assertTrue("User detail call should not fail", false)
                latch.countDown()
            }
        })

        latch.await(200, TimeUnit.MILLISECONDS)
    }

    @Test
    fun testUserDetailFailedCall() {
        server.enqueue(failedResponse)
        val latch = CountDownLatch(1)
        client.getUserDetails("test", object : ClientCallback<GithubUser> {
            override fun onSuccess(payload: GithubUser) {
                assertTrue("User detail call should not succeed", false)
                latch.countDown()
            }

            override fun onFailure(error: ApiError) {
                assertEquals(ApiErrorType.FAILED, error.type)
                latch.countDown()
            }
        })

        latch.await(200, TimeUnit.MILLISECONDS)
    }

    // ======= User Repos Test =======
    @Test
    fun testUserRepoDetailAccuracy() {
        server.enqueue(reposResponse)
        val latch = CountDownLatch(1)
        client.getUserRepo("test", object : ClientCallback<GithubRepos> {
            override fun onSuccess(payload: GithubRepos) {
                assertEquals(1, payload.size)

                assertEquals(testRepos[0].name, payload[0].name)
                assertEquals(testRepos[0].forks, payload[0].forks)
                assertEquals(testRepos[0].stargazers, payload[0].stargazers)
                assertEquals(testRepos[0].description, payload[0].description)

                latch.countDown()
            }

            override fun onFailure(error: ApiError) {
                assertTrue("User detail call should not fail", false)
                latch.countDown()
            }
        })

        latch.await(200, TimeUnit.MILLISECONDS)
    }

    @Test
    fun testHandleRepoCallFailure() {
        server.enqueue(failedResponse)
        val latch = CountDownLatch(1)
        client.getUserRepo("test", object : ClientCallback<GithubRepos> {
            override fun onSuccess(payload: GithubRepos) {
                assertTrue("Repo call should fail", false)
                latch.countDown()
            }

            override fun onFailure(error: ApiError) {
                assertEquals(ApiErrorType.FAILED, error.type)
                latch.countDown()
            }
        })

        latch.await(200, TimeUnit.MILLISECONDS)
    }
}