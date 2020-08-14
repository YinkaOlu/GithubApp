package com.yinkaolu.githubapp.view.fragment.repolist

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yinkaolu.githubapp.R
import com.yinkaolu.githubapp.data.model.GithubRepo

class RepoListItemHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var titleTextView: TextView = itemView.findViewById(R.id.repoTitle)
    var descTextView: TextView = itemView.findViewById(R.id.repoDesc)

    fun onBind(repo: GithubRepo) {
        titleTextView.text = repo.name
        descTextView.text = repo.description
    }
}