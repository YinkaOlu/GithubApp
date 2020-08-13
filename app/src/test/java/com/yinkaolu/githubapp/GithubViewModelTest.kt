package com.yinkaolu.githubapp

import com.yinkaolu.githubapp.data.repository.GithubRepository
import com.yinkaolu.githubapp.viewmodel.GithubViewModel
import org.junit.Test
import org.mockito.Mockito.*

class GithubViewModelTest {
    @Test
    fun testViewModelLoadData() {
        val repo = mock(GithubRepository::class.java)
        val viewmodel = GithubViewModel(repo)

        viewmodel.loadUserData("test")

        verify(repo).loadUser(MockitoHelper.any())
        verify(repo).loadUserRepo(MockitoHelper.any())
    }

    @Test
    fun testViewModelLoadSameDataOnce() {
        val repo = mock(GithubRepository::class.java)
        val viewmodel = GithubViewModel(repo)

        viewmodel.loadUserData("test")
        viewmodel.loadUserData("test")

        verify(repo, times(1)).loadUser(MockitoHelper.any())
        verify(repo, times(1)).loadUserRepo(MockitoHelper.any())
    }

    @Test
    fun testViewModelLoadsNewData() {
        val repo = mock(GithubRepository::class.java)
        val viewmodel = GithubViewModel(repo)

        viewmodel.loadUserData("test")
        viewmodel.loadUserData("test2")

        verify(repo, times(2)).loadUser(MockitoHelper.any())
        verify(repo, times(2)).loadUserRepo(MockitoHelper.any())
    }

    @Test
    fun testViewModelReloadSameDataForced() {
        val repo = mock(GithubRepository::class.java)
        val viewmodel = GithubViewModel(repo)

        viewmodel.loadUserData("test")
        viewmodel.loadUserData("test", true)

        verify(repo, times(2)).loadUser(MockitoHelper.any())
        verify(repo, times(2)).loadUserRepo(MockitoHelper.any())
    }
}