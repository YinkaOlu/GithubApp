package com.yinkaolu.githubapp

import com.yinkaolu.githubapp.data.repository.GithubRepository
import com.yinkaolu.githubapp.viewmodel.GithubViewModel
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

class GithubViewModelTest {
    @Test
    fun testViewModelLoadData() {
        val repo = mock(GithubRepository::class.java)
        val viewmodel = GithubViewModel(repo)

        viewmodel.getUser("test")

        verify(repo).loadUser(MockitoHelper.any(), ArgumentMatchers.eq(true))
        verify(repo).loadUserRepo(MockitoHelper.any(), ArgumentMatchers.eq(true))
    }

    @Test
    fun testViewModelBlocksEmptyUsername() {
        val repo = mock(GithubRepository::class.java)
        val viewmodel = GithubViewModel(repo)

        viewmodel.getUser("")

        verify(repo, never()).loadUser(MockitoHelper.any(), ArgumentMatchers.eq(true))
        verify(repo, never()).loadUserRepo(MockitoHelper.any(), ArgumentMatchers.eq(true))
    }

    @Test
    fun testViewModelLoadsNewData() {
        val repo = mock(GithubRepository::class.java)
        val viewmodel = GithubViewModel(repo)

        viewmodel.getUser("test")
        viewmodel.getUser("test2")

        verify(repo, times(2)).loadUser(MockitoHelper.any(), ArgumentMatchers.eq(true))
        verify(repo, times(2)).loadUserRepo(MockitoHelper.any(), ArgumentMatchers.eq(true))
    }

    @Test
    fun testViewModelNotConsiderCacheOnFreshLoad() {
        val repo = mock(GithubRepository::class.java)
        val viewmodel = GithubViewModel(repo)

        viewmodel.getUser("test", true)

        verify(repo).loadUser(MockitoHelper.any(), ArgumentMatchers.eq(false))
        verify(repo).loadUserRepo(MockitoHelper.any(), ArgumentMatchers.eq(false))
    }
}