package com.yinkaolu.githubapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yinkaolu.githubapp.data.provider.ProviderError
import com.yinkaolu.githubapp.data.provider.ProviderErrorType
import com.yinkaolu.githubapp.data.provider.DataProviderCallback
import com.yinkaolu.githubapp.data.provider.GithubDataProvider
import com.yinkaolu.githubapp.data.model.GithubRepo
import com.yinkaolu.githubapp.data.model.GithubRepos
import com.yinkaolu.githubapp.data.model.GithubUser
import com.yinkaolu.githubapp.data.repository.DefaultGithubRepository
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Tests to cover GithubRepository handling of data from Data Provider and input data
 */
class GithubRepositoryTest {
    private val testUser = GithubUser("test", "/path")
    private val testRepos = GithubRepos()
    private val mockClient = mock(GithubDataProvider::class.java)
    private val githubRepository = DefaultGithubRepository(mockClient)

    @Before
    fun setUp() {
        testRepos.add(GithubRepo("test1", updatedAt = "2020-08-10T15:08:32Z", stargazers = 1, forks = 2))
        testRepos.add(GithubRepo("test2", updatedAt = "2020-06-10T15:08:32Z", stargazers = 2, forks = 3))

    }

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testCorrectApiCall_loadUser() {
        githubRepository.loadUser("test")

        val captor = ArgumentCaptor.forClass(String::class.java)
        // Verify repository is passing the correct user name to data source
        verify(mockClient).getUserDetails(ArgumentCaptorHelper.capture<String>(captor), MockitoHelper.any())
        assertEquals( "test", captor.value)
    }

    @Test
    fun testDataSaved_loadUser() {
        Mockito.`when`(
            mockClient.getUserDetails(MockitoHelper.any(), MockitoHelper.any())
        ).thenAnswer {
            (it.arguments[1] as DataProviderCallback<GithubUser>).onSuccess(testUser)
        }

        val latch = CountDownLatch(1)

        githubRepository.loadUser("test")
        githubRepository.currentUser.observeForever { user ->
            user?.let { safeUser ->
                Assert.assertEquals(testUser.name, safeUser.name)
                Assert.assertEquals(testUser.avatarURL, safeUser.avatarURL)
                latch.countDown()
            }
        }

        latch.await(200, TimeUnit.MILLISECONDS)
    }

    @Test
    fun testRepositoryHandlesError_loadUser() {
        Mockito.`when`(
            mockClient.getUserDetails(MockitoHelper.any(), MockitoHelper.any())
        ).thenAnswer {
            (it.arguments[1] as DataProviderCallback<GithubUser>).onFailure(ProviderError(ProviderErrorType.FAILED))
        }

        val latch = CountDownLatch(1)

        githubRepository.loadUser("test")
        githubRepository.userError.observeForever { error ->
            Assert.assertEquals(ProviderErrorType.FAILED, error?.type)
            latch.countDown()
        }

        latch.await(200, TimeUnit.MILLISECONDS)
    }

    @Test
    fun testCorrectApiCall_loadUserRepo() {
        githubRepository.loadUserRepo("test")

        val captor = ArgumentCaptor.forClass(String::class.java)
        // Verify repository is passing the correct user name to data source
        verify(mockClient).getUserRepo(ArgumentCaptorHelper.capture<String>(captor), MockitoHelper.any())
        assertEquals( "test", captor.value)
    }

    @Test
    fun testDataSaved_loadUserRepo() {
        Mockito.`when`(
            mockClient.getUserRepo(MockitoHelper.any(), MockitoHelper.any())
        ).thenAnswer {
            (it.arguments[1] as DataProviderCallback<List<GithubRepo>>).onSuccess(testRepos)
        }

        val latch = CountDownLatch(1)

        githubRepository.loadUserRepo("test")
        githubRepository.currentRepoList.observeForever { repoList ->

            repoList?.let {safeList ->
                Assert.assertEquals(2, safeList.size)

                Assert.assertEquals(testRepos[0].name, safeList[0].name)
                Assert.assertEquals(testRepos[0].forks, safeList[0].forks)
                Assert.assertEquals(testRepos[0].stargazers, safeList[0].stargazers)
                Assert.assertEquals(testRepos[0].description, safeList[0].description)

                Assert.assertEquals(testRepos[1].name, safeList[1].name)
                Assert.assertEquals(testRepos[1].forks, safeList[1].forks)
                Assert.assertEquals(testRepos[1].stargazers, safeList[1].stargazers)
                Assert.assertEquals(testRepos[1].description, safeList[1].description)
                latch.countDown()
            }
        }

        latch.await(200, TimeUnit.MILLISECONDS)
    }

    @Test
    fun testRepositoryHandlesError_loadUserRepo() {
         Mockito.`when`(
            mockClient.getUserRepo(MockitoHelper.any(), MockitoHelper.any())
        ).thenAnswer {
            (it.arguments[1] as DataProviderCallback<List<GithubRepo>>).onFailure(ProviderError(ProviderErrorType.FAILED))
        }

        val latch = CountDownLatch(1)

        githubRepository.loadUserRepo("test")
        githubRepository.repositoryError.observeForever { error ->
            Assert.assertEquals(ProviderErrorType.FAILED, error?.type)
            latch.countDown()
        }

        latch.await(200, TimeUnit.MILLISECONDS)
    }
}