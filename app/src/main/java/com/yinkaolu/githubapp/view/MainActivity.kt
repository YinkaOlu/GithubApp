package com.yinkaolu.githubapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.yinkaolu.githubapp.R
import com.yinkaolu.githubapp.data.provider.ProviderError
import com.yinkaolu.githubapp.data.repository.DataState
import com.yinkaolu.githubapp.view.fragment.error.ErrorFragment
import com.yinkaolu.githubapp.view.fragment.repolist.RepoListFragment
import com.yinkaolu.githubapp.viewmodel.GithubViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: GithubViewModel
    private lateinit var quickFadeAnimation: Animation
    private lateinit var medFadeAnimation: Animation
    private lateinit var longFadeAnimation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(GithubViewModel::class.java)
        quickFadeAnimation = AnimationUtils.loadAnimation(this, R.anim.quick_fade_in)
        medFadeAnimation = AnimationUtils.loadAnimation(this, R.anim.med_fade_in)
        longFadeAnimation = AnimationUtils.loadAnimation(this, R.anim.long_fade_in)

        observerUserData()
        searchBtn.setOnClickListener {
            viewModel.getUser(editSearch.text.toString())
        }
    }

    private fun observerUserData() {
        viewModel.user?.observe(this) { user ->
            user?.let { safeUser ->
                userName.startAnimation(medFadeAnimation)
                userName.text = safeUser.name
                Glide.with(this).load(safeUser.avatarURL).into(userAvatar)
                userAvatar.startAnimation(quickFadeAnimation)

            }
        }

        viewModel.repos.observe(this) { repo ->
            repo?.let { safeRepo ->
                val repoFrag = RepoListFragment.instance(safeRepo)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.listContainer, repoFrag)
                    .commit()
                listContainer.startAnimation(longFadeAnimation)
            }
        }

        viewModel.userApiError.observe(this) { handleError(it) }
        viewModel.repoApiError.observe(this) { handleError(it) }
        viewModel.state.observe(this) {
            showProgress(it)
        }
    }

    private fun showProgress(dataState: DataState) {
        if (dataState === DataState.LOADING) {
            loadingBar.visibility = View.VISIBLE
        } else {
            loadingBar.visibility = View.GONE
        }
    }

    private fun handleError(error: ProviderError?) {
        error?.let {
            userName.text = ""
            Glide.with(this).load(getString(R.string.error_image)).into(userAvatar)
            val frag = ErrorFragment.instance(it.message)
            supportFragmentManager.beginTransaction()
                .replace(R.id.listContainer, frag)
                .commit()
        }
    }
}