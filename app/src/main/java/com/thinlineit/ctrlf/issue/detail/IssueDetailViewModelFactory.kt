package com.thinlineit.ctrlf.issue.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thinlineit.ctrlf.entity.Issue

class IssueDetailViewModelFactory(private val issueInfo: Issue) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IssueDetailViewModel::class.java)) {
            return IssueDetailViewModel(issueInfo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
