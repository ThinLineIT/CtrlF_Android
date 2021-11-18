package com.thinlineit.ctrlf.page.editor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.ActivityPageEditorBinding
import com.thinlineit.ctrlf.entity.Page
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.observeIfNotHandled
import kotlinx.android.synthetic.main.activity_page_editor.pager
import kotlinx.android.synthetic.main.activity_page_editor.tabLayout

class PageEditorActivity : FragmentActivity() {
    private lateinit var pageEditorAdapter: PageEditorAdapter

    private val binding: ActivityPageEditorBinding by lazy {
        DataBindingUtil.setContentView(
            this,
            R.layout.activity_page_editor
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_editor)
        val pageInfo = intent.getParcelableExtra(PAGE_INFO) ?: Page()
        val topicTitle = intent.getStringExtra(TOPIC_TITLE) ?: ""
        val topicId = intent.getIntExtra(TOPIC_ID, 0)
        val viewModelFactory = PageEditorViewModelFactory(pageInfo, topicTitle, topicId)
        val viewModel =
            ViewModelProvider(this, viewModelFactory).get(PageEditorViewModel::class.java)

        binding.apply {
            this.viewModel = viewModel
            lifecycleOwner = this@PageEditorActivity
        }

        pageEditorAdapter = PageEditorAdapter(this).apply {
            addFragment(PageEditFragment())
            addFragment(PagePreviewFragment())
        }

        pager.adapter = pageEditorAdapter

        })

        viewModel.createPageStatus.observeIfNotHandled(this) {
            if (it == Status.SUCCESS) {
                PageEditorCompleteDialog(this).show()
            }
        }
    }

    companion object {
        const val PAGE_INFO = "pageInfo"
        const val TOPIC_TITLE = "topicTitle"
        const val TOPIC_ID = "topicId"

        fun start(context: Context) {
            val intent = Intent(context, PageEditorActivity::class.java)
            context.startActivity(intent)
        }
    }
}
