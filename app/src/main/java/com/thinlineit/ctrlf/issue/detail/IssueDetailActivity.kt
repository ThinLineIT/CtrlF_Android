package com.thinlineit.ctrlf.issue.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.ActivityIssueDetailBinding
import com.thinlineit.ctrlf.entity.UNSET_ID
import com.thinlineit.ctrlf.page.detail.PageActivity
import com.thinlineit.ctrlf.page.editor.PageEditorActivity
import com.thinlineit.ctrlf.registration.signout.LogoutActivity
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.changeVisibilityState
import com.thinlineit.ctrlf.util.observeIfNotHandled

class IssueDetailActivity : AppCompatActivity() {

    private val binding: ActivityIssueDetailBinding by lazy {
        ActivityIssueDetailBinding.inflate(layoutInflater)
    }
    private val issueUpdateClickListener: IssueUpdateClickListener by lazy {
        IssueUpdateClickListener(this, viewModel)
    }
    lateinit var viewModel: IssueDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val issueId = intent.getIntExtra(ISSUE_ID, 0)
        val viewModelFactory = IssueDetailViewModelFactory(issueId)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(IssueDetailViewModel::class.java)
        binding.issueDetailViewModel = viewModel
        binding.lifecycleOwner = this
        init()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadIssue()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.userCircleBtn -> {
            val intent = Intent(this, LogoutActivity::class.java)
            startActivity(intent)
            true
        }
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun init() {
        setToolbar()
        initClickListener()
        initObserveViewModel()
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolBar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun initClickListener() {
        binding.apply {
            detailButton.setOnClickListener {
                val issue = viewModel.issue.value ?: return@setOnClickListener
                PageActivity.start(
                    it.context,
                    issue.noteId ?: UNSET_ID,
                    issue.topicId ?: UNSET_ID,
                    issue.pageId ?: UNSET_ID,
                    issue.versionNo ?: UNSET_ID
                )
            }

            moreActionButton.setOnClickListener {
                issueMoreBox.changeVisibilityState()
            }

            issueUpdateButton.setOnClickListener {
                val issue = viewModel.issue.value ?: return@setOnClickListener
                val hasPermission =
                    viewModel.issuePermissionStatus.value ?: return@setOnClickListener
                if (hasPermission) {
                    when {
                        issue.action == DELETE -> {
                            issueUpdateClickListener.onDeleteUpdateClick()
                        }
                        issue.relatedModelType == PAGE -> {
                            val pageInfo = viewModel.pageInfo.value ?: return@setOnClickListener
                            val topicInfo = viewModel.topicInfo.value ?: return@setOnClickListener
                            val issue = viewModel.issue.value ?: return@setOnClickListener
                            val intent = Intent(
                                this@IssueDetailActivity,
                                PageEditorActivity::class.java
                            ).apply {
                                putExtra(PageEditorActivity.PAGE, pageInfo)
                                putExtra(PageEditorActivity.TOPIC_TITLE, topicInfo.title)
                                putExtra(PageEditorActivity.SUMMARY, issue.reason)
                                putExtra(PageEditorActivity.MODE, PageEditorActivity.Mode.UPDATE)
                            }
                            startActivity(intent)
                        }
                        else -> {
                            issueUpdateClickListener.onDefaultUpdateClick()
                        }
                    }
                } else {
                    Toast.makeText(
                        this@IssueDetailActivity,
                        R.string.notice_update_fail,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun initObserveViewModel() {
        viewModel.apply {
            detailButtonStatus.observe(this@IssueDetailActivity) {
                toggleViewVisibility(
                    binding.detailButton,
                    it
                )
            }
            approveButtonStatus.observe(this@IssueDetailActivity) {
                toggleViewVisibility(binding.approveButton, it)
                toggleViewVisibility(binding.rejectButton, it)
            }
            issueApproveStatus.observeIfNotHandled(this@IssueDetailActivity) {
                if (it == Status.SUCCESS) {
                    Toast.makeText(
                        this@IssueDetailActivity,
                        R.string.notice_approve_success,
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else
                    Toast.makeText(
                        this@IssueDetailActivity,
                        R.string.notice_approve_fail,
                        Toast.LENGTH_LONG
                    ).show()
            }
            issueRejectStatus.observeIfNotHandled(this@IssueDetailActivity) {
                if (it == Status.SUCCESS) {
                    Toast.makeText(
                        this@IssueDetailActivity,
                        R.string.notice_reject_success,
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else
                    Toast.makeText(
                        this@IssueDetailActivity,
                        R.string.notice_reject_fail,
                        Toast.LENGTH_LONG
                    ).show()
            }
            issueDeleteStatus.observeIfNotHandled(this@IssueDetailActivity) {
                if (it == Status.SUCCESS) {
                    Toast.makeText(
                        this@IssueDetailActivity,
                        R.string.notice_delete_success,
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else
                    Toast.makeText(
                        this@IssueDetailActivity,
                        R.string.notice_delete_fail,
                        Toast.LENGTH_LONG
                    ).show()
            }
            issueCloseStatus.observeIfNotHandled(this@IssueDetailActivity) {
                if (it == Status.SUCCESS) {
                    Toast.makeText(
                        this@IssueDetailActivity,
                        R.string.notice_close_success,
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else
                    Toast.makeText(
                        this@IssueDetailActivity,
                        R.string.notice_close_fail,
                        Toast.LENGTH_LONG
                    ).show()
            }
            issueInfoStatus.observe(this@IssueDetailActivity) {
                if (!it) finish()
            }
        }
    }

    private fun toggleViewVisibility(
        view: View,
        visible: Boolean
    ) {
        view.visibility =
            if (visible)
                View.VISIBLE
            else
                View.GONE
    }

    companion object {
        const val ISSUE_ID = "issueId"
        const val PAGE = "PAGE"
        const val DELETE = "DELETE"
        fun start(context: Context, issueId: Int) {
            val intent = Intent(context, IssueDetailActivity::class.java).apply {
                putExtra(ISSUE_ID, issueId)
                addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            }
            context.startActivity(intent)
        }
    }
}
