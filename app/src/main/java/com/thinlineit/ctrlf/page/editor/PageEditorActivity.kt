package com.thinlineit.ctrlf.page.editor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.ActivityPageEditorBinding
import com.thinlineit.ctrlf.entity.Page
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.observeIfNotHandled
import kotlinx.android.synthetic.main.activity_page_editor.*

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

        replaceFragment(PageEditFragment())
        setTabBackground(
            R.drawable.background_left_selected_tab_purple,
            R.drawable.background_right_unselected_tab_white
        )

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        val editFragment = PageEditFragment.newInstance()
                        replaceFragment(editFragment)
                        setTabBackground(
                            R.drawable.background_left_selected_tab_purple,
                            R.drawable.background_right_unselected_tab_white
                        )
                    }
                    1 -> {
                        val previewFragment = PagePreviewFragment.newInstance()
                        replaceFragment(previewFragment)
                        setTabBackground(
                            R.drawable.background_left_unselected_tab_white,
                            R.drawable.background_right_selected_tab_purple
                        )
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        viewModel.createPageStatus.observeIfNotHandled(this) {
            if (it == Status.SUCCESS) {
                PageEditorCompleteDialog(this).show()
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.editorContainer, fragment)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            commit()
        }
    }

    private fun setTabBackground(tab1: Int, tab2: Int) {
        val editTab = tabLayout.getChildAt(0)
        val previewTab = tabLayout.getChildAt(1)
        if (editTab != null) {
            ViewCompat.setBackground(editTab, AppCompatResources.getDrawable(editTab.context, tab1))
        }
        if (previewTab != null) {
            ViewCompat.setBackground(
                previewTab,
                AppCompatResources.getDrawable(previewTab.context, tab2)
            )
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
