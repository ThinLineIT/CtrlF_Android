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
        val pageInfo = intent.getParcelableExtra("pageInfo") ?: Page()
        val viewModelFactory = PageEditorViewModelFactory(pageInfo)
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

        TabLayoutMediator(tabLayout, pager) { tab, position ->
            if (position == 0) tab.setText(R.string.button_edit)
            else tab.setText(R.string.button_preview)
        }.attach()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, PageEditorActivity::class.java)
            context.startActivity(intent)
        }
    }
}
