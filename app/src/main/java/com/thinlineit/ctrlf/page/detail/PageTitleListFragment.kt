package com.thinlineit.ctrlf.page.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.thinlineit.ctrlf.databinding.FragmentPageTitleBinding
import com.thinlineit.ctrlf.page.editor.PageEditorActivity

class PageTitleListFragment : Fragment() {
    private val pageTitleListAdapter = PageTitleListAdapter { page ->
        pageViewModel.selectPage(Pair(page.id, page.versionNo))
    }
    private val swipeController = SwipeController()
    private val itemTouchHelper = ItemTouchHelper(swipeController)
    private val pageViewModel by activityViewModels<PageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPageTitleBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            this.pageViewModel = this@PageTitleListFragment.pageViewModel
            lifecycleOwner = this@PageTitleListFragment
            pageListRecyclerView.adapter = pageTitleListAdapter
            itemTouchHelper.attachToRecyclerView(pageListRecyclerView)
            pageListRecyclerView.layoutManager =
                LinearLayoutManager(this@PageTitleListFragment.context)
            pageTitleListAdapter.setSwipeButtonClickListener(
                PageTitleSwipeButtonClickListener(
                    requireContext(),
                    this@PageTitleListFragment.pageViewModel
                )
            )
        }

        binding.addPageBtn.setOnClickListener {
            val topic = pageViewModel.topic.value ?: return@setOnClickListener
            val intent = Intent(activity, PageEditorActivity::class.java).apply {
                putExtra(PageEditorActivity.TOPIC_ID, topic.id)
                putExtra(PageEditorActivity.TOPIC_TITLE, topic.title)
                putExtra(PageEditorActivity.MODE, PageEditorActivity.Mode.CREATE)
            }
            startActivity(intent)
        }
        pageViewModel.topic.observe(viewLifecycleOwner) {
            if (it == null) {
                findNavController().navigate(
                    PageTitleListFragmentDirections.actionPageFragmentToTopicFragment()
                )
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        pageViewModel.selectTopic(pageViewModel.topic.value?.id ?: return)
    }
}
