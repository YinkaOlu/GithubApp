package com.yinkaolu.githubapp

import com.yinkaolu.githubapp.data.api.*
import com.yinkaolu.githubapp.data.model.GithubRepo
import com.yinkaolu.githubapp.data.model.GithubUser
import okhttp3.ResponseBody
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import retrofit2.Response
import java.util.concurrent.CountDownLatch
import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito.mock
import retrofit2.Call
import java.io.IOException
import java.lang.RuntimeException


class RetrofitGithubClientTest {
    private lateinit var testUser: GithubUser
    private lateinit var testRepos: List<GithubRepo>
    private lateinit var mockUserCall: Call<GithubUser>
    private lateinit var mockRepoCall: Call<List<GithubRepo>>
    private lateinit var api: GithubAPI

    @Before
    fun setUp() {
        testUser = GithubUser("test", "/path")
        testRepos = arrayListOf(
            GithubRepo("test1", updatedAt = "2020-08-10T15:08:32Z", stargazers = 1, forks = 2),
            GithubRepo("test2", updatedAt = "2020-07-10T15:08:32Z", stargazers = 2, forks = 3)
        )
        mockUserCall = mock(Call::class.java) as Call<GithubUser>
        mockRepoCall = mock(Call::class.java) as Call<List<GithubRepo>>
        api = mock(GithubAPI::class.java)

        Mockito.`when`(
            api.getUser(any(String::class.java))
        ).thenReturn(mockUserCall)

        Mockito.`when`(
            api.getUserRepo(any(String::class.java))
        ).thenReturn(mockRepoCall)
    }

    // ======= User detail Test =======
    @Test
    fun testUserDetailAccuracy() {
        val validUserResponse = Response.success(testUser)

        Mockito.`when`(
            mockUserCall.execute()
        ).thenReturn(validUserResponse)

        val client = RetrofitGithubClient(api)

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

        latch.await()
    }

    @Test
    fun testUserDetailFailedCall() {
        val failedResponse = Response.error<GithubUser>(400, ResponseBody.create(null, ""))
        val api: GithubAPI = mock(GithubAPI::class.java)

        Mockito.`when`(
            mockUserCall.execute()
        ).thenReturn(failedResponse)

        val client = RetrofitGithubClient(api)

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

        latch.await()
    }

    @Test
    fun testHandlingCallIOException() {
        val api: GithubAPI = mock(GithubAPI::class.java)

        Mockito.`when`(
            mockUserCall.execute()
        ).thenThrow(IOException())

        val client = RetrofitGithubClient(api)

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

        latch.await()
    }

    @Test
    fun testHandlingCallRuntimeException() {
        val api: GithubAPI = mock(GithubAPI::class.java)

        Mockito.`when`(
            mockUserCall.execute()
        ).thenThrow(RuntimeException())

        val client = RetrofitGithubClient(api)

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

        latch.await()
    }

    // ======= User Repos Test =======
    @Test
    fun testUserRepoDetailAccuracy() {
        val validRepoResponse = Response.success(testRepos)

        Mockito.`when`(
            mockRepoCall.execute()
        ).thenReturn(validRepoResponse)

        val client = RetrofitGithubClient(api)

        val latch = CountDownLatch(1)
        client.getUserRepo("test", object : ClientCallback<List<GithubRepo>> {
            override fun onSuccess(payload: List<GithubRepo>) {
                assertEquals(2, payload.size)

                assertEquals(testRepos[0].name, payload[0].name)
                assertEquals(testRepos[0].forks, payload[0].forks)
                assertEquals(testRepos[0].stargazers, payload[0].stargazers)
                assertEquals(testRepos[0].description, payload[0].description)

                assertEquals(testRepos[1].name, payload[1].name)
                assertEquals(testRepos[1].forks, payload[1].forks)
                assertEquals(testRepos[1].stargazers, payload[1].stargazers)
                assertEquals(testRepos[1].description, payload[1].description)

                latch.countDown()
            }

            override fun onFailure(error: ApiError) {
                assertTrue("User detail call should not fail", false)
                latch.countDown()
            }
        })

        latch.await()
    }

    @Test
    fun testHandleRepoCallFailure() {
        val failedResponse = Response.error<List<GithubRepo>>(400, ResponseBody.create(null, ""))

        Mockito.`when`(
            mockRepoCall.execute()
        ).thenReturn(failedResponse)

        val client = RetrofitGithubClient(api)

        val latch = CountDownLatch(1)
        client.getUserRepo("test", object : ClientCallback<List<GithubRepo>> {
            override fun onSuccess(payload: List<GithubRepo>) {
                assertTrue("Repo call should fail", false)
                latch.countDown()
            }

            override fun onFailure(error: ApiError) {
                assertEquals(ApiErrorType.FAILED, error.type)
                latch.countDown()
            }
        })

        latch.await()
    }

    @Test
    fun testHandleRepoIOException() {
        Mockito.`when`(
            mockRepoCall.execute()
        ).thenThrow(IOException())

        val client = RetrofitGithubClient(api)

        val latch = CountDownLatch(1)
        client.getUserRepo("test", object : ClientCallback<List<GithubRepo>> {
            override fun onSuccess(payload: List<GithubRepo>) {
                assertTrue("Repo call should fail", false)
                latch.countDown()
            }

            override fun onFailure(error: ApiError) {
                assertEquals(ApiErrorType.FAILED, error.type)
                latch.countDown()
            }
        })

        latch.await()
    }

    @Test
    fun testHandleRepoRuntimeException() {
        Mockito.`when`(
            mockRepoCall.execute()
        ).thenThrow(RuntimeException())

        val client = RetrofitGithubClient(api)

        val latch = CountDownLatch(1)
        client.getUserRepo("test", object : ClientCallback<List<GithubRepo>> {
            override fun onSuccess(payload: List<GithubRepo>) {
                assertTrue("Repo call should fail", false)
                latch.countDown()
            }

            override fun onFailure(error: ApiError) {
                assertEquals(ApiErrorType.FAILED, error.type)
                latch.countDown()
            }
        })

        latch.await()
    }
}