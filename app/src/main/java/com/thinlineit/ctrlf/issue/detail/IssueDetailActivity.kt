package com.thinlineit.ctrlf.issue.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.thinlineit.ctrlf.MainActivity
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.ActivityIssueDetailBinding
import com.thinlineit.ctrlf.databinding.ActivityIssueDetailBindingImpl
import com.thinlineit.ctrlf.issue.Issue
import com.thinlineit.ctrlf.page.detail.PageActivity
import com.thinlineit.ctrlf.registration.signout.LogoutActivity

class IssueDetailActivity : AppCompatActivity() {

    private val binding: ActivityIssueDetailBinding by lazy {
        ActivityIssueDetailBindingImpl.inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val issue = intent.getSerializableExtra(ISSUE_INFO) as Issue
        val viewModelFactory = IssueDetailViewModelFactory(issue)
        val issueDetailViewModel =
            ViewModelProvider(this, viewModelFactory).get(IssueDetailViewModel::class.java)

        binding.apply {
            setSupportActionBar(toolBar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            this.issueDetailViewModel = issueDetailViewModel
            lifecycleOwner = this@IssueDetailActivity
            detailButton.setOnClickListener {
                val intent = Intent(this@IssueDetailActivity, PageActivity::class.java)
                intent.putExtra("pageId", issueDetailViewModel.issueInfo.value!!.id)
                startActivity(intent)
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
        else -> super.onOptionsItemSelected(item)
    }

    companion object {
        const val ISSUE_INFO = "issueInfo"

        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}
