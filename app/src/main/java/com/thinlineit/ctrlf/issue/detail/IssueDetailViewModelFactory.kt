package com.thinlineit.ctrlf.issue.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class IssueDetailViewModelFactory(private val issueId: Int) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IssueDetailViewModel::class.java)) {
            return IssueDetailViewModel(issueId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
