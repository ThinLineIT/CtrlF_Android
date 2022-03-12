package com.thinlineit.ctrlf.page.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.thinlineit.ctrlf.databinding.FragmentTopicTitleBinding

class TopicTitleListFragment : Fragment() {
    private val topicTitleListAdapter = TopicTitleListAdapter { topic ->
        pageViewModel.selectTopic(topic.id)
    }
    private val swipeController = SwipeController()
    private val itemTouchHelper = ItemTouchHelper(swipeController)
    private val pageViewModel by activityViewModels<PageViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTopicTitleBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            this.pageViewModel = this@TopicTitleListFragment.pageViewModel
            lifecycleOwner = this@TopicTitleListFragment
            topicListRecyclerView.adapter = topicTitleListAdapter.apply {
                setSwipeBtnClickListener(
                    TopicTitleSwipeButtonClickListener(
                        requireContext(),
                        this@TopicTitleListFragment.pageViewModel
                    )
                )
            }
            itemTouchHelper.attachToRecyclerView(topicListRecyclerView)
            topicListRecyclerView.layoutManager =
                LinearLayoutManager(this@TopicTitleListFragment.context)

            seeMoreBoxOfNoteBtn.setOnClickListener {
                when (seeMoreBoxOfNote.visibility) {
                    View.GONE -> seeMoreBoxOfNote.visibility = View.VISIBLE
                    else -> seeMoreBoxOfNote.visibility = View.GONE
                }
            }
            updateNoteTitleBtn.setOnClickListener {
                NoteUpdateClickListener(
                    requireContext(),
                    this@TopicTitleListFragment.pageViewModel
                ).onUpdate()
            }

            deleteNoteBtn.setOnClickListener {
                NoteDeleteClickListener(
                    requireContext(),
                    this@TopicTitleListFragment.pageViewModel
                ).onDelete()
            }

            addTopicBtn.setOnClickListener {
                TopicCreateClickListener(
                    requireContext(),
                    this@TopicTitleListFragment.pageViewModel
                ).onCreateTopic()
            }
        }
        pageViewModel.topic.observe(viewLifecycleOwner) {
            if (it != null) {
                this.findNavController().navigate(
                    TopicTitleListFragmentDirections.actionNotesFragmentToPageFragment()
                )
            }
        }
        return binding.root
    }
}
