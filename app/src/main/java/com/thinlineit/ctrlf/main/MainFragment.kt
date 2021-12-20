package com.thinlineit.ctrlf.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.thinlineit.ctrlf.databinding.FragmentMainBinding
import com.thinlineit.ctrlf.main.banner.MainBannerViewPagerAdapter

class MainFragment : Fragment() {

    private val mainViewModel by viewModels<MainViewModel>()
    private val noteAdapter = MainNoteAdapter { noteId ->
        this.findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToPageActivity(noteId)
        )
    }
    private val issueAdapter = MainIssueAdapter { issueInfo ->
        this.findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToIssueDetailActivity(issueInfo)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            mainViewModel = this@MainFragment.mainViewModel
            lifecycleOwner = this@MainFragment
            mainViewPager.adapter = MainBannerViewPagerAdapter(requireActivity())
            noteListRecyclerView.adapter = noteAdapter
            issueListRecyclerView.adapter = issueAdapter

            showAllNoteTextView.setOnClickListener {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToNotesFragment()
                )
            }
            showAllIssueTextView.setOnClickListener {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToIssueListFragment()
                )
            }

            this@MainFragment.mainViewModel.issueList.observe(viewLifecycleOwner) {
                updateIssueViewVisibility(
                    issueListRecyclerView,
                    issueEmptyText,
                    it.isEmpty()
                )
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.loadNote()
        mainViewModel.loadIssue()
    }

    private fun updateIssueViewVisibility(
        recyclerView: RecyclerView,
        textView: TextView,
        isEmpty: Boolean
    ) {
        if (isEmpty) {
            recyclerView.visibility = View.GONE
            textView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            textView.visibility = View.GONE
        }
    }
}
