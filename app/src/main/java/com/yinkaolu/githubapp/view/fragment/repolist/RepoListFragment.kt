package com.yinkaolu.githubapp.view.fragment.repolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yinkaolu.githubapp.R
import com.yinkaolu.githubapp.data.model.GithubRepo
import java.lang.IllegalArgumentException

class RepoListFragment : Fragment(), RepoListItemListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_repo_list, container, false)
        val repoList = view.findViewById<RecyclerView>(R.id.repoList)

        val repos: ArrayList<GithubRepo>? = arguments?.getParcelableArrayList(REPO_ARGS_KEY)

        if (repos === null) throw IllegalArgumentException("Repo list fragment requires bundle with ArrayList of GithubRepo")

        val repoListAdapter = RepoListAdapter(repos, this)
        repoList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = repoListAdapter
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

    override fun onSelected(repo: GithubRepo) {
        val repoBottomSheet = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.repo_bottom_sheet, null)

        // Add repo details
        bottomSheetView.findViewById<TextView>(R.id.repoForks).text = "${repo.forks}"
        bottomSheetView.findViewById<TextView>(R.id.repoStars).text = "${repo.stargazers}"
        bottomSheetView.findViewById<TextView>(R.id.repoLastUpdated).text = repo.updatedAt

        repoBottomSheet.setContentView(bottomSheetView)
        repoBottomSheet.show()
    }
}