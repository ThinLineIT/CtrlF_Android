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
import com.thinlineit.ctrlf.R
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
            updateNoteTitleBtn.setOnClickListener { _ ->
                context?.let {
                    CreateDialog(
                        it,
                        resources.getString(R.string.hint_dialog_note_title_modify),
                        resources.getString(R.string.hint_dialog_note_title_hint_modify)
                    ) { title, reason ->
                        this@TopicTitleListFragment.pageViewModel.updateNote(title, reason)
                    }.openDialog()
                }
            }

            deleteNoteBtn.setOnClickListener {
                NoteDeleteClickListener(
                    requireContext(),
                    this@TopicTitleListFragment.pageViewModel
                ).onDelete()
            }

            addTopicBtn.setOnClickListener { _ ->
                context?.let {
                    CreateDialog(
                        it,
                        resources.getString(R.string.hint_dialog_topic_title),
                        resources.getString(R.string.hint_dialog_topic_title_hint)
                    ) { title, reason ->
                        this@TopicTitleListFragment.pageViewModel.createTopic(title, reason)
                    }.openDialog()
                }
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
