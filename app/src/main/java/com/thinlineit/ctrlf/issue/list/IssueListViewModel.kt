package com.thinlineit.ctrlf.issue.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.thinlineit.ctrlf.issue.IssueDao
import com.thinlineit.ctrlf.repository.IssueRepository
import com.thinlineit.ctrlf.util.base.ListLiveData

class IssueListViewModel : ViewModel() {
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
        // TODO: Load the list of issue using "getIssue" api
        _issueList.value = issueRepository.loadIssueList()
    }
}
