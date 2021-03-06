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
import com.yinkaolu.githubapp.data.model.GithubRepos
import java.lang.IllegalArgumentException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

/**
 * Fragment to display Repository list data
 */
class RepoListFragment : Fragment(), RepoListItemListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_repo_list, container, false)
        val repoList = view.findViewById<RecyclerView>(R.id.repoList)

        val repos: GithubRepos? = arguments?.getParcelableArrayList<GithubRepo>(REPO_ARGS_KEY) as GithubRepos

        if (repos === null) throw IllegalArgumentException("Repo list fragment requires bundle with ArrayList of GithubRepo")

        val repoListAdapter = RepoListAdapter(repos, this)
        repoList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = repoListAdapter
        }
        return view
    }

    companion object {
        private const val REPO_ARGS_KEY = "REPO_ARGS_KEY"
        val dateStringFormat = SimpleDateFormat("MMMM dd, yyyy h:mm:ss a", Locale.CANADA)
        fun instance(repos: GithubRepos): RepoListFragment {
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

        repo.updatedDate?.let {
            bottomSheetView.findViewById<TextView>(R.id.repoLastUpdated).text = dateStringFormat.format(it)
        }

        repoBottomSheet.setContentView(bottomSheetView)
        repoBottomSheet.show()
    }
}