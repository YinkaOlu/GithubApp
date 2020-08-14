package com.yinkaolu.githubapp.view.fragment.repolist

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yinkaolu.githubapp.R
import com.yinkaolu.githubapp.data.model.GithubRepo

interface RepoListItemListener {
    fun onSelected(repo: GithubRepo)
}

class RepoListItemHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var titleTextView: TextView = itemView.findViewById(R.id.repoTitle)
    private var descTextView: TextView = itemView.findViewById(R.id.repoDesc)

    fun onBind(repo: GithubRepo, listener: RepoListItemListener) {
        titleTextView.text = repo.name
        descTextView.text = repo.description
        itemView.setOnClickListener {listener.onSelected(repo)}
    }
}