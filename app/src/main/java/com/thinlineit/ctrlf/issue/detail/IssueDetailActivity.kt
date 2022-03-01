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
import com.thinlineit.ctrlf.registration.signout.LogoutActivity
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.changeVisibilityState
import com.thinlineit.ctrlf.util.observeIfNotHandled

class IssueDetailActivity : AppCompatActivity() {

    private val binding: ActivityIssueDetailBinding by lazy {
        ActivityIssueDetailBinding.inflate(layoutInflater)
    }
    lateinit var issueDetailViewModel: IssueDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val issueId = intent.getIntExtra(ISSUE_ID, 0)
        val viewModelFactory = IssueDetailViewModelFactory(issueId)
        issueDetailViewModel =
            ViewModelProvider(this, viewModelFactory).get(IssueDetailViewModel::class.java)
        binding.issueDetailViewModel = issueDetailViewModel
        binding.lifecycleOwner = this
        init()
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
                val issue = issueDetailViewModel?.issue?.value ?: return@setOnClickListener
                PageActivity.start(
                    it.context,
                    issue.noteId ?: UNSET_ID,
                    issue.topicId ?: UNSET_ID,
                    issue.pageId ?: UNSET_ID,
                    issue.versionNo ?: UNSET_ID
                )
            }

            rejectButton.setOnClickListener {
                Toast.makeText(
                    this@IssueDetailActivity,
                    R.string.notice_service_prepare,
                    Toast.LENGTH_LONG
                ).show()
            }

            moreActionButton.setOnClickListener {
                issueMoreBox.changeVisibilityState()
            }

            issueUpdateButton.setOnClickListener {
                // TODO :
            }
        }
    }

    private fun initObserveViewModel() {
        issueDetailViewModel.apply {
            detailButtonStatus.observe(this@IssueDetailActivity) {
                updateViewVisibility(
                    binding.detailButton,
                    it
                )
            }
            approveButtonStatus.observe(this@IssueDetailActivity) {
                updateTwoViewVisibility(
                    binding.approveButton,
                    binding.rejectButton,
                    it
                )
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
            toolbarTitle.observe(this@IssueDetailActivity) {
                if (it == R.string.empty_text) finish()
            }
        }
    }

    private fun updateViewVisibility(
        view: View,
        visible: Boolean
    ) {
        view.visibility =
            if (visible)
                View.VISIBLE
            else
                View.GONE
    }

    private fun updateTwoViewVisibility(
        firstView: View,
        secondView: View,
        visible: Boolean
    ) {
        updateViewVisibility(firstView, visible)
        updateViewVisibility(secondView, visible)
    }

    companion object {
        const val ISSUE_ID = "issueId"
        const val PAGE = "PAGE"
        fun start(context: Context, issueId: Int) {
            val intent = Intent(context, IssueDetailActivity::class.java).apply {
                putExtra(ISSUE_ID, issueId)
            }
            context.startActivity(intent)
        }
    }
}
