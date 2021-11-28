package com.thinlineit.ctrlf.page.editor

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Toast
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
import com.thinlineit.ctrlf.util.CustomDialogInterface
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.observeIfNotHandled
import kotlinx.android.synthetic.main.activity_page_editor.*

class PageEditorActivity : FragmentActivity(), CustomDialogInterface {
    private lateinit var pageEditorAdapter: PageEditorAdapter
    private val leftSelected by lazy {
        AppCompatResources.getDrawable(this, R.drawable.background_left_selected_tab_purple)
    }
    private val leftUnSelected by lazy {
        AppCompatResources.getDrawable(this, R.drawable.background_left_unselected_tab_white)
    }
    private val rightSelected by lazy {
        AppCompatResources.getDrawable(this, R.drawable.background_right_selected_tab_purple)
    }
    private val rightUnSelected by lazy {
        AppCompatResources.getDrawable(this, R.drawable.background_right_unselected_tab_white)
    }

    private val editTab by lazy {
        (tabLayout.getChildAt(0) as ViewGroup).getChildAt(0)
    }
    private val previewTab by lazy {
        (tabLayout.getChildAt(0) as ViewGroup).getChildAt(1)
    }

    private val binding: ActivityPageEditorBinding by lazy {
        DataBindingUtil.setContentView(
            this,
            R.layout.activity_page_editor
        )
    }

    @SuppressLint("ClickableViewAccessibility")
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
        setTabBackground(leftSelected, rightUnSelected)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        val editFragment = PageEditFragment.newInstance()
                        replaceFragment(editFragment)
                        setTabBackground(leftSelected, rightUnSelected)
                    }
                    1 -> {
                        val previewFragment = PagePreviewFragment.newInstance()
                        replaceFragment(previewFragment)
                        setTabBackground(leftUnSelected, rightSelected)
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
                PageEditorCompleteDialog(this, this).show()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.notice_invalid_add_page),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        summary.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            return@setOnTouchListener false
        }

        cancelButton.setOnClickListener {
            PageEditorCancelDialog(this, this).show()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.editorContainer, fragment)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            commit()
        }
    }

    private fun setTabBackground(editTabBackground: Drawable?, previewTabBackground: Drawable?) {
        ViewCompat.setBackground(editTab, editTabBackground)
        ViewCompat.setBackground(previewTab, previewTabBackground)
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

    override fun onFinishButton() {
        finish()
    }
}
