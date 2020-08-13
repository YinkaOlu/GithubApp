package com.yinkaolu.githubapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yinkaolu.githubapp.viewmodel.GithubViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GithubViewModel().loadUserData("octocat")
    }
}