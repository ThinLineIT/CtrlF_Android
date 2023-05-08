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
import com.thinlineit.ctrlf.util.LoadingDialog
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.checkImgPermission
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
    val pageEditorViewModel: PageEditorViewModel by lazy {
        val pageInfo = intent.getParcelableExtra(PAGE) ?: Page()
        val topicTitle = intent.getStringExtra(TOPIC_TITLE) ?: ""
        val topicId = intent.getIntExtra(TOPIC_ID, 0)
        val summary = intent.getStringExtra(SUMMARY) ?: ""
        val mode = intent.getSerializableExtra(MODE)
        val viewModelFactory =
            PageEditorViewModelFactory(pageInfo, topicTitle, topicId, summary, mode as Mode)
        ViewModelProvider(this, viewModelFactory).get(PageEditorViewModel::class.java).apply {
            toolboxController = ToolboxController(binding.root.findViewById(R.id.toolbox))
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_editor)
        binding.apply {
            viewModel = pageEditorViewModel
            lifecycleOwner = this@PageEditorActivity
        }
        initView()
        checkImgPermission(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        val editFragment = PageEditFragment.newInstance()
        val previewFragment = PagePreviewFragment.newInstance()

        pageEditorAdapter = PageEditorAdapter(this).apply {
            addFragment(editFragment)
            addFragment(previewFragment)
        }
        replaceFragment(editFragment)
        setTabBackground(leftSelected, rightUnSelected)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        replaceFragment(editFragment)
                        setTabBackground(leftSelected, rightUnSelected)
                    }
                    1 -> {
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

        pageEditorViewModel.editPageStatus.observeIfNotHandled(this) {
            if (it == Status.SUCCESS) {
                PageEditorDialog(this, this, R.layout.dialog_create_issue).show()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.notice_invalid_add_page),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        val loadingDialog = LoadingDialog(this)

        pageEditorViewModel.isLoading.observe(this) {
            if (it) loadingDialog.show()
            else loadingDialog.dismiss()
        }

        summary.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            return@setOnTouchListener false
        }

        binding.cancelButton.setOnClickListener {
            PageEditorDialog(this, this, R.layout.dialog_cancel_editor).show()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.editorContainer, fragment)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            commit()
        }
    }

    private fun setTabBackground(
        editTabBackground: Drawable?,
        previewTabBackground: Drawable?
    ) {
        ViewCompat.setBackground(editTab, editTabBackground)
        ViewCompat.setBackground(previewTab, previewTabBackground)
    }

    companion object {
        const val TOPIC_TITLE = "topicTitle"
        const val TOPIC_ID = "topicId"
        const val PAGE = "page"
        const val SUMMARY = "summary"
        const val MODE = "mode"

        fun start(context: Context) {
            val intent = Intent(context, PageEditorActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onBackPressed() {
        PageEditorDialog(this, this, R.layout.dialog_cancel_editor).show()
    }

    override fun onFinishButton() {
        finish()
    }

    enum class Mode {
        CREATE, EDIT, UPDATE
    }
}
