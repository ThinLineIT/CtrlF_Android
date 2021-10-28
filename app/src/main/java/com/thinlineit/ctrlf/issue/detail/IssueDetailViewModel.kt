package com.thinlineit.ctrlf.issue.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.entity.Issue
import com.thinlineit.ctrlf.repository.dao.IssueRepository
import com.thinlineit.ctrlf.util.base.BaseViewModel

class IssueDetailViewModel(issueId: Int) : BaseViewModel() {
    private val issueRepository by lazy {
        IssueRepository()
    }

    private val _issueId = MutableLiveData<Int>(issueId)
    val issueId: LiveData<Int>
        get() = _issueId

    private val _issueInfo = MutableLiveData<Issue>()
    val issueInfo: LiveData<Issue>
        get() = _issueInfo

    init {
        loadIssue(issueId)
    }

    fun loadIssue(issueId: Int) {
        viewModelScope.loadingLaunch {
            try {
                _issueInfo.value = issueRepository.getIssueDetail(issueId.toString())
            } catch (e: Exception) {
            }
        }
    }
}
