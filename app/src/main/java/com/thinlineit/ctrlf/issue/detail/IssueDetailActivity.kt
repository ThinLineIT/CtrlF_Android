package com.thinlineit.ctrlf.issue.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.ActivityIssueDetailBinding
import com.thinlineit.ctrlf.entity.UNSET_ID
import com.thinlineit.ctrlf.page.detail.PageActivity
import com.thinlineit.ctrlf.registration.signout.LogoutActivity

class IssueDetailActivity : AppCompatActivity() {

    private val binding: ActivityIssueDetailBinding by lazy {
        ActivityIssueDetailBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            Log.d("testttt", "tttt")
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    companion object {
        const val ISSUE_ID = "issueId"

        fun start(context: Context, issueId: Int) {
            val intent = Intent(context, IssueDetailActivity::class.java).apply {
                putExtra(ISSUE_ID, issueId)
            }
            context.startActivity(intent)
        }
    }
}
