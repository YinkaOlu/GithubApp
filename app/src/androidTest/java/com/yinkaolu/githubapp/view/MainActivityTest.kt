package com.yinkaolu.githubapp.view

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.yinkaolu.githubapp.R
import com.yinkaolu.githubapp.data.provider.RetrofitDataProvider
import com.yinkaolu.githubapp.data.model.GithubRepos
import com.yinkaolu.githubapp.data.model.GithubUser
import com.yinkaolu.githubapp.view.fragment.repolist.RepoListFragment
import com.yinkaolu.githubapp.view.fragment.repolist.RepoListItemHolder
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.net.HttpURLConnection

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    lateinit var server: MockWebServer
    private val userJsonElement: JsonElement = JsonParser().parse(GithubTestJson.testUserJsonString)
    private val testUser: GithubUser = Gson().fromJson(userJsonElement, GithubUser::class.java)

    private val reposJsonElement: JsonElement = JsonParser().parse(GithubTestJson.testReposJsonString)
    private val testRepos: GithubRepos = Gson().fromJson(reposJsonElement, GithubRepos::class.java)

    private val userResponse = MockResponse()
    private val reposResponse = MockResponse()
    private val failedResponse = MockResponse()

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        // Direct Client to Mock server
        RetrofitDataProvider.instance.set(server.url("").toString())

        userResponse.setBody(GithubTestJson.testUserJsonString)
        reposResponse.setBody(GithubTestJson.testReposJsonString)

        failedResponse.setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
    }

    @Test
    fun testLoadingData() {
        //Enter search test
        enterSearchText("octocat")
        // Check progress views are showing
        checkProgressView()
    }

    @Test
    fun testDataLoaded() {
        server.enqueue(userResponse)
        server.enqueue(reposResponse)

        //Enter search test
        enterSearchText("octocat")
        // Check user details views are showing
        checkUserDetail()
        checkRepoDetails()
    }

    @Test
    fun testDataFailedToLoad() {
        server.enqueue(failedResponse)

        //Enter search test
        enterSearchText("octocat")
        // Check failed view
        checkFailure()
    }

    private fun enterSearchText(input: String) {
        onView(withId(R.id.editSearch))
            .perform(typeText(input))
        closeSoftKeyboard()
        onView(withId(R.id.searchBtn))
            .perform(click())
    }

    private fun checkProgressView() {
        onView(withId(R.id.loadingBar))
            .check(matches(isDisplayed()))
    }

    private fun checkUserDetail() {
        onView(withId(R.id.userName))
            .check(matches(withText(testUser.name)))
    }

    private fun checkRepoDetails() {
        testRepos.forEach { repo ->
            onView(withId(R.id.repoList))
                .perform(
                    RecyclerViewActions.scrollTo<RepoListItemHolder>(
                        hasDescendant(withText(repo.name))
                    ))
            onView(withText(repo.name))
                .perform(click())

            onView(withId(R.id.repoStars))
                .check(matches(withText("${repo.stargazers}")))
            onView(withId(R.id.repoForks))
                .check(matches(withText("${repo.forks}")))
            onView(withId(R.id.repoLastUpdated))
                .check(matches(withText(RepoListFragment.dateStringFormat.format(repo.updatedDate))))
        }
    }

    private fun checkFailure() {
        onView(withId(R.id.errorTitle))
            .check(matches(withText(activityRule.activity.baseContext.getString(R.string.error_message_tittle))))

        onView(withId(R.id.errorMessage))
            .check(matches(isDisplayed()))
    }


}