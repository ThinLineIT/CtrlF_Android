package com.thinlineit.ctrlf.page.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.thinlineit.ctrlf.databinding.FragmentPageTitleBinding
import com.thinlineit.ctrlf.page.editor.PageEditorActivity

class PageTitleListFragment : Fragment() {
    private val pageTitleListAdapter = PageTitleListAdapter { pageId ->
        pageViewModel.openPage(pageId)
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
        }

        binding.addPageBtn.setOnClickListener {
            val intent = Intent(activity, PageEditorActivity::class.java)
            intent.putExtra(PageEditorActivity.TOPIC_ID, pageViewModel.topicIdInfo.value)
            intent.putExtra(PageEditorActivity.TOPIC_TITLE, pageViewModel.topicTitleTop.value)

            startActivity(intent)
        }
        return binding.root
    }
}
