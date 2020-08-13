package com.yinkaolu.githubapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yinkaolu.githubapp.data.api.ApiError
import com.yinkaolu.githubapp.data.api.ApiErrorType
import com.yinkaolu.githubapp.data.api.ClientCallback
import com.yinkaolu.githubapp.data.api.GithubAPIClient
import com.yinkaolu.githubapp.data.model.GithubRepo
import com.yinkaolu.githubapp.data.model.GithubUser
import com.yinkaolu.githubapp.data.repository.DefaultGithubRepository
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


class GithubRepositoryTest {
    private val testUser = GithubUser("test", "/path")
    private val testRepos = arrayListOf(
        GithubRepo("test1", updatedAt = "2020-08-10T15:08:32Z", stargazers = 1, forks = 2),
        GithubRepo("test2", updatedAt = "2020-06-10T15:08:32Z", stargazers = 2, forks = 3)
    )

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testLoadPlayerCallsCorrectAPI() {
        val mockClient = mock(GithubAPIClient::class.java)
        val repo = DefaultGithubRepository(mockClient)

        repo.loadUser("test")

        val captor = ArgumentCaptor.forClass(String::class.java)
        // Verify repository is passing the correct user name to data source
        verify(mockClient).getUserDetails(ArgumentCaptorHelper.capture<String>(captor), MockitoHelper.any())
        assertEquals( "test", captor.value)
    }

    @Test
    fun testLoadUserSaveData() {
        val mockClient = mock(GithubAPIClient::class.java)
        val repo = DefaultGithubRepository(mockClient)

        Mockito.`when`(
            mockClient.getUserDetails(MockitoHelper.any(), MockitoHelper.any())
        ).thenAnswer {
            (it.arguments[1] as ClientCallback<GithubUser>).onSuccess(testUser)
        }

        val latch = CountDownLatch(1)

        repo.loadUser("test")
        repo.currentUser.observeForever { user ->
            Assert.assertEquals(testUser.name, user.name)
            Assert.assertEquals(testUser.avatarURL, user.avatarURL)
            latch.countDown()
        }

        latch.await(200, TimeUnit.MILLISECONDS)
    }

    @Test
    fun testLoadUserHandlesError() {
        val mockClient = mock(GithubAPIClient::class.java)
        val repo = DefaultGithubRepository(mockClient)

        Mockito.`when`(
            mockClient.getUserDetails(MockitoHelper.any(), MockitoHelper.any())
        ).thenAnswer {
            (it.arguments[1] as ClientCallback<GithubUser>).onFailure(ApiError(ApiErrorType.FAILED))
        }

        val latch = CountDownLatch(1)

        repo.loadUser("test")
        repo.userApiError.observeForever { error ->
            Assert.assertEquals(ApiErrorType.FAILED, error?.type)
            latch.countDown()
        }

        latch.await(200, TimeUnit.MILLISECONDS)
    }

    @Test
    fun testLoadRepoCallsCorrectAPI() {
        val mockClient = mock(GithubAPIClient::class.java)
        val repo = DefaultGithubRepository(mockClient)

        repo.loadUserRepo("test")

        val captor = ArgumentCaptor.forClass(String::class.java)
        // Verify repository is passing the correct user name to data source
        verify(mockClient).getUserRepo(ArgumentCaptorHelper.capture<String>(captor), MockitoHelper.any())
        assertEquals( "test", captor.value)
    }

    @Test
    fun testLoadRepoPassesCorrectAPI() {
        val mockClient = mock(GithubAPIClient::class.java)
        val repo = DefaultGithubRepository(mockClient)

        Mockito.`when`(
            mockClient.getUserRepo(MockitoHelper.any(), MockitoHelper.any())
        ).thenAnswer {
            (it.arguments[1] as ClientCallback<List<GithubRepo>>).onSuccess(testRepos)
        }

        val latch = CountDownLatch(1)

        repo.loadUserRepo("test")
        repo.currentRepoList.observeForever { repoList ->
            Assert.assertEquals(2, repoList.size)

            Assert.assertEquals(testRepos[0].name, repoList[0].name)
            Assert.assertEquals(testRepos[0].forks, repoList[0].forks)
            Assert.assertEquals(testRepos[0].stargazers, repoList[0].stargazers)
            Assert.assertEquals(testRepos[0].description, repoList[0].description)

            Assert.assertEquals(testRepos[1].name, repoList[1].name)
            Assert.assertEquals(testRepos[1].forks, repoList[1].forks)
            Assert.assertEquals(testRepos[1].stargazers, repoList[1].stargazers)
            Assert.assertEquals(testRepos[1].description, repoList[1].description)
            latch.countDown()
        }

        latch.await(200, TimeUnit.MILLISECONDS)
    }

    @Test
    fun testLoadReposHandlesError() {
        val mockClient = mock(GithubAPIClient::class.java)
        val repo = DefaultGithubRepository(mockClient)

        Mockito.`when`(
            mockClient.getUserRepo(MockitoHelper.any(), MockitoHelper.any())
        ).thenAnswer {
            (it.arguments[1] as ClientCallback<List<GithubRepo>>).onFailure(ApiError(ApiErrorType.FAILED))
        }

        val latch = CountDownLatch(1)

        repo.loadUserRepo("test")
        repo.repoApiError.observeForever { error ->
            Assert.assertEquals(ApiErrorType.FAILED, error?.type)
            latch.countDown()
        }

        latch.await(200, TimeUnit.MILLISECONDS)
    }
}