package com.yinkaolu.githubapp.view.fragment.repolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yinkaolu.githubapp.R
import com.yinkaolu.githubapp.data.model.GithubRepo

class RepoListAdapter(
    private var repoList: ArrayList<GithubRepo>,
    private var listener: RepoListItemListener
): RecyclerView.Adapter<RepoListItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoListItemHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.repo_list_item, parent, false)
        return RepoListItemHolder(itemView)
    }

    override fun getItemCount(): Int {
        return repoList.size
    }

    override fun onBindViewHolder(holder: RepoListItemHolder, position: Int) {
        holder.onBind(repoList[position], listener)
    }
}