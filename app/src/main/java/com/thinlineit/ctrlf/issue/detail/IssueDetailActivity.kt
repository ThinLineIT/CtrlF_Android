package com.thinlineit.ctrlf.issue.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.ActivityIssueDetailBinding
import com.thinlineit.ctrlf.entity.UNSET_ID
import com.thinlineit.ctrlf.page.detail.PageActivity
import com.thinlineit.ctrlf.registration.signout.LogoutActivity
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.observeIfNotHandled

class IssueDetailActivity : AppCompatActivity() {

    private val binding: ActivityIssueDetailBinding by lazy {
        ActivityIssueDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val issueId = intent.getIntExtra(ISSUE_ID, 0)
        val viewModelFactory = IssueDetailViewModelFactory(issueId)
        val issueDetailViewModel =
            ViewModelProvider(this, viewModelFactory).get(IssueDetailViewModel::class.java)

        binding.apply {
            setSupportActionBar(toolBar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowTitleEnabled(false)
            }
            this.issueDetailViewModel = issueDetailViewModel
            lifecycleOwner = this@IssueDetailActivity
            detailButton.setOnClickListener {
                val issue = issueDetailViewModel.issue.value ?: return@setOnClickListener
                PageActivity.start(
                    it.context,
                    issue.noteId ?: UNSET_ID,
                    issue.topicId ?: UNSET_ID,
                    issue.pageId ?: UNSET_ID
                )
            }

            issueDetailViewModel.apply {
                detailButtonStatus.observe(this@IssueDetailActivity) {
                    updateDetailButtonViewVisibility(
                        detailButton,
                        it
                    )
                }
                approveButtonStatus.observe(this@IssueDetailActivity) {
                    updateApproveButtonViewVisibility(
                        approveButton,
                        rejectButton,
                        it
                    )
                }
            }
        }

        issueDetailViewModel.issueApproveStatus.observeIfNotHandled(this) {
            if (it == Status.SUCCESS) {
                Toast.makeText(this, R.string.notice_complete_approve, Toast.LENGTH_LONG).show()
                finish()
            } else
                Toast.makeText(this, R.string.notice_non_authority, Toast.LENGTH_LONG).show()
        }

        issueDetailViewModel.toolbarTitle.observe(this) {
            if (it == R.string.empty_text) finish()
        }
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

    private fun updateDetailButtonViewVisibility(
        buttonView: Button,
        visible: Boolean
    ) {
        if (visible) {
            buttonView.visibility = View.VISIBLE
        } else {
            buttonView.visibility = View.GONE
        }
    }

    private fun updateApproveButtonViewVisibility(
        approveButtonView: Button,
        rejectButtonView: Button,
        visible: Boolean
    ) {
        if (visible) {
            approveButtonView.visibility = View.VISIBLE
            rejectButtonView.visibility = View.VISIBLE
        } else {
            approveButtonView.visibility = View.GONE
            rejectButtonView.visibility = View.GONE
        }
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
