package com.yinkaolu.githubapp.view.fragment.error

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yinkaolu.githubapp.R

class ErrorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_error, container, false)
        val errorMsg = arguments?.getString(ERROR_MSG_KEY, "")
        view.findViewById<TextView>(R.id.errorMessage).text = errorMsg

        return view
    }

    companion object {
        private const val ERROR_MSG_KEY = "ERROR_MSG_KEY"
        fun instance(errorMsg: String? = "") =
            ErrorFragment().apply {
                arguments = Bundle().apply {
                    putString(ERROR_MSG_KEY, errorMsg)
                }
            }
    }
}