package com.thinlineit.ctrlf.page

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
import kotlinx.android.synthetic.main.fragment_page_title.*

class PageTitleListFragment : Fragment() {
    private val pageTitleListAdapter = PageTitleListAdapter { pageId ->
        pageViewModel.openSliding()
        pageViewModel.openPage(pageId)
    }
    private val itemTouchHelper = ItemTouchHelper(SwipeController(pageTitleListAdapter))
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
            intent.putExtra(TOPIC_ID, pageViewModel.topicIdInfo.value)
            intent.putExtra(TOPIC_INFO, pageViewModel.topicTitleTop.value)

            startActivity(intent)
        }
        return binding.root
    }

    companion object {
        const val TOPIC_ID = "topicId"
        const val TOPIC_INFO = "topicTitle"
    }
}
