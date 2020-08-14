package com.yinkaolu.githubapp.view.fragment.repolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yinkaolu.githubapp.R
import com.yinkaolu.githubapp.data.model.GithubRepo
import java.lang.IllegalArgumentException

class RepoListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_repo_list, container, false)
        val repoList = view.findViewById<RecyclerView>(R.id.repoList)

        val repos: ArrayList<GithubRepo>? = arguments?.getParcelableArrayList(REPO_ARGS_KEY)

        if (repos === null) throw IllegalArgumentException("Repo list fragment requires bundle with ArrayList of GithubRepo")

        repoList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = RepoListAdapter(repos)
        }
        return view
    }

    companion object {
        const val REPO_ARGS_KEY = "REPO_ARGS_KEY"
        fun instance(repos: ArrayList<GithubRepo>): RepoListFragment {
            val frag =
                RepoListFragment()
            val repoBundle = Bundle()
            repoBundle.putParcelableArrayList(REPO_ARGS_KEY, repos)
            frag.arguments = repoBundle
            return frag
        }
    }
}