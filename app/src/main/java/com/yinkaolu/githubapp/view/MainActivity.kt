package com.yinkaolu.githubapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.squareup.picasso.Picasso
import com.yinkaolu.githubapp.R
import com.yinkaolu.githubapp.data.repository.DataState
import com.yinkaolu.githubapp.view.fragment.repolist.RepoListFragment
import com.yinkaolu.githubapp.viewmodel.GithubViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: GithubViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(GithubViewModel::class.java)

        viewModel.user?.observe(this){ user ->
            user?.let { safeUser ->
                userName.text = safeUser.name
                Picasso.get().load(safeUser.avatarURL).into(userAvatar);
            }
        }

        viewModel.repos.observe(this) {repo ->
            repo?.let {safeRepo ->
                val repoFrag = RepoListFragment.instance(safeRepo)
                supportFragmentManager.beginTransaction()
                    .add(R.id.listContainer, repoFrag)
                    .commit()
            }
        }

        viewModel.userApiError.observe(this) {
            // TODO: Show User load error
        }

        viewModel.repoApiError.observe(this) {
            // TODO: Show Repo load error
        }

        viewModel.state.observe(this) {
            showProgress(it)
        }

        searchBtn.setOnClickListener {
            viewModel.getUser(editSearch.text.toString())
        }
    }

    private fun showProgress(dataState: DataState) {
        if (dataState === DataState.LOADING) {
            loadingBar.visibility = View.VISIBLE
        } else {
            loadingBar.visibility = View.GONE
        }
    }
}