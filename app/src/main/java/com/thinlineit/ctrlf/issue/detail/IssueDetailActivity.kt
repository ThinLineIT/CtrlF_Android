package com.thinlineit.ctrlf.issue.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.ActivityIssueDetailBinding
import com.thinlineit.ctrlf.issue.Issue

class IssueDetailActivity : AppCompatActivity() {

    private val binding: ActivityIssueDetailBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_issue_detail)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val issue = intent.getSerializableExtra(ISSUE_INFO) as Issue
        val viewModelFactory = IssueDetailViewModelFactory(issue)
        val issueDetailViewModel =
            ViewModelProvider(this, viewModelFactory).get(IssueDetailViewModel::class.java)
        binding.apply {
            this.issueDetailViewModel = issueDetailViewModel
            lifecycleOwner = this@IssueDetailActivity
        }
    }

    companion object {
        const val ISSUE_INFO = "issueInfo"
    }
}
