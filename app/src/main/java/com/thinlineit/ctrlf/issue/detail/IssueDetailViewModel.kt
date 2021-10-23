package com.thinlineit.ctrlf.issue.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thinlineit.ctrlf.issue.Issue

class IssueDetailViewModel(issue: Issue) : ViewModel() {
    private val _issueInfo = MutableLiveData<Issue>(issue)
    val issueInfo: LiveData<Issue>
        get() = _issueInfo
}
