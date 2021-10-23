package com.thinlineit.ctrlf.issue.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.issue.IssueDao
import com.thinlineit.ctrlf.repository.IssueRepository
import com.thinlineit.ctrlf.util.base.BaseViewModel
import com.thinlineit.ctrlf.util.base.ListLiveData

class IssueListViewModel : BaseViewModel() {
    private val issueRepository by lazy {
        IssueRepository()
    }

    private val _issueList = ListLiveData<IssueDao>()
    val issueList: LiveData<List<IssueDao>>
        get() = _issueList

    init {
        loadIssue()
    }

    private fun loadIssue() {
        viewModelScope.loadingLaunch {
            try {
                _issueList.value = issueRepository.loadIssueList()
            } catch (e: Exception) {
            }
        }
    }
}
