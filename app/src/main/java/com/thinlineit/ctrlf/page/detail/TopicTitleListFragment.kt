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
            topicListRecyclerView.adapter = topicTitleListAdapter
            itemTouchHelper.attachToRecyclerView(topicListRecyclerView)
            topicListRecyclerView.layoutManager =
                LinearLayoutManager(this@TopicTitleListFragment.context)
            // TODO: 툴바 이미지 변경, 클릭 시 준비중입니다 다이얼로그 적용
            addTopicBtn.setOnClickListener { _ ->
                val dialog = CreateDialog(
                    resources.getString(R.string.hint_dialog_topic_title)
                ) { title, reason ->
                    pageViewModel?.createTopic(title, reason)
                }
                activity?.supportFragmentManager?.let { fragmentManager ->
                    dialog.show(
                        fragmentManager,
                        ADD_TOPIC
                    )
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
    companion object {
        const val ADD_TOPIC = "add topic"
    }
}
