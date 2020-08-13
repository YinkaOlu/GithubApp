package com.yinkaolu.githubapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.yinkaolu.githubapp.data.repository.DataState
import com.yinkaolu.githubapp.viewmodel.GithubViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: GithubViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(GithubViewModel::class.java)

        viewModel.user?.observe(this){
            // TODO: Show user
        }

        viewModel.repos.observe(this) {
            // TODO: Show repo
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

    fun showProgress(dataState: DataState) {
        if (dataState === DataState.LOADING) {
            loadingBar.visibility = View.VISIBLE
        } else {
            loadingBar.visibility = View.GONE
        }
    }
}