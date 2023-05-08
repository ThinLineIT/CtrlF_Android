package com.thinlineit.ctrlf.issue.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.thinlineit.ctrlf.databinding.FragmentIssueListBinding

class IssueListFragment : Fragment() {
    private val issueViewModel by viewModels<IssueListViewModel>()
    private val issueAdapter = IssueListAdapter { issueId ->
        this.findNavController().navigate(
            IssueListFragmentDirections.actionIssueListFragmentToIssueDetailActivity(issueId)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentIssueListBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            this.issueViewModel = this@IssueListFragment.issueViewModel
            lifecycleOwner = this@IssueListFragment
            issueListRecyclerView.adapter = issueAdapter
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        issueViewModel.loadIssue()
    }
}
