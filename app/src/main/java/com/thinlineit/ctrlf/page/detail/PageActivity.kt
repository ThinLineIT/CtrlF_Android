package com.thinlineit.ctrlf.page.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.ActivityPageBinding
import com.thinlineit.ctrlf.entity.UNSET_ID
import com.thinlineit.ctrlf.issue.detail.IssueDetailActivity
import com.thinlineit.ctrlf.page.editor.PageEditorActivity
import com.thinlineit.ctrlf.page.editor.PageEditorActivity.Companion.MODE
import com.thinlineit.ctrlf.page.editor.PageEditorActivity.Companion.PAGE
import com.thinlineit.ctrlf.page.editor.PageEditorActivity.Companion.TOPIC_TITLE
import com.thinlineit.ctrlf.registration.signout.LogoutActivity
import com.thinlineit.ctrlf.util.LoadingDialog
import kotlinx.android.synthetic.main.activity_page.bookMarkButton
import kotlinx.android.synthetic.main.activity_page.editButton
import kotlinx.android.synthetic.main.activity_page.fabButton
import kotlinx.android.synthetic.main.activity_page.fabChildButtonList
import kotlinx.android.synthetic.main.activity_page.pageActivityToolBar
import kotlinx.android.synthetic.main.activity_page.relatedIssueButton
import kotlinx.android.synthetic.main.activity_page.shareButton
import kotlinx.android.synthetic.main.activity_page.slidingPaneLayout

class PageActivity : AppCompatActivity() {
    private val pageViewModel by viewModels<PageViewModel>()
    private val binding: ActivityPageBinding by lazy {
        ActivityPageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            setContentView(root)
            pageViewModel = this@PageActivity.pageViewModel
            lifecycleOwner = this@PageActivity
        }
        val noteId = intent.getIntExtra(NOTE_ID, UNSET_ID)
        val topicId = intent.getIntExtra(TOPIC_ID, UNSET_ID)
        val pageId = intent.getIntExtra(PAGE_ID, UNSET_ID)
        val versionNo = intent.getIntExtra(VERSION_NO, UNSET_ID)
        pageViewModel.setPageHierarchy(noteId, topicId, Pair(pageId, versionNo))

        initObserver()
        initButton()
        FloatingMenuUIController(this, pageViewModel.isFabOpen, fabButton, fabChildButtonList)

        setSupportActionBar(pageActivityToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun initButton() {
        fabButton.setOnClickListener {
            pageViewModel.toggleFab()
        }

        shareButton.setOnClickListener {
            // TODO: copy the uri on clipboard
            Toast.makeText(this, R.string.notice_service_prepare, Toast.LENGTH_SHORT).show()
        }
        bookMarkButton.setOnClickListener {
            // TODO: save this page as bookmark
            Toast.makeText(this, R.string.notice_service_prepare, Toast.LENGTH_SHORT).show()
        }
        relatedIssueButton.setOnClickListener {
            val issueId = pageViewModel.page.value?.issueId
            if (issueId != null)
                IssueDetailActivity.start(this, issueId)
            else
                Toast.makeText(this, R.string.notice_non_exist_related_issue, Toast.LENGTH_SHORT)
                    .show()
        }
        editButton.setOnClickListener {
            val topic = pageViewModel.topic.value ?: return@setOnClickListener
            val intent = Intent(this, PageEditorActivity::class.java).apply {
                putExtra(PAGE, pageViewModel.page.value)
                putExtra(TOPIC_TITLE, topic.title)
                putExtra(MODE, PageEditorActivity.Mode.EDIT)
            }
            startActivity(intent)
        }
    }

    private fun initObserver() {
        pageViewModel.isRightPaneOpen.observe(this) {
            if (it == true && slidingPaneLayout.isSlideable) {
                slidingPaneLayout.openPane()
            } else {
                slidingPaneLayout.closePane()
            }
        }

        val loadingDialog = LoadingDialog(this)
        pageViewModel.isLoading.observe(this) {
            if (it) loadingDialog.show()
            else loadingDialog.dismiss()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.userCircleBtn -> {
            val logout = Intent(this, LogoutActivity::class.java)
            startActivity(logout)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!pageViewModel.onBackPressed())
            finish()
    }

    companion object {
        const val NOTE_ID = "noteId"
        const val TOPIC_ID = "topicId"
        const val PAGE_ID = "pageId"
        const val VERSION_NO = "versionNo"

        fun start(
            context: Context,
            noteId: Int,
            topicId: Int = UNSET_ID,
            pageId: Int = UNSET_ID,
            versionNo: Int = UNSET_ID
        ) {
            val intent = Intent(context, PageActivity::class.java).apply {
                putExtra(NOTE_ID, noteId)
                putExtra(TOPIC_ID, topicId)
                putExtra(PAGE_ID, pageId)
                putExtra(VERSION_NO, versionNo)
                addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            }
            context.startActivity(intent)
        }
    }
}
