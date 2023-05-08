package com.thinlineit.ctrlf.issue.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.entity.Issue
import com.thinlineit.ctrlf.repository.dao.IssueRepository
import com.thinlineit.ctrlf.util.base.BaseViewModel

class IssueListViewModel(
    private val issueRepository: IssueRepository = IssueRepository()
) : BaseViewModel() {

    private val _issueList = MutableLiveData<List<Issue>>(emptyList())
    val issueList: LiveData<List<Issue>>
        get() = _issueList

    init {
        loadIssue()
    }

    fun loadIssue() {
        viewModelScope.loadingLaunch {
            try {
                _issueList.value = issueRepository.loadIssueList()
            } catch (e: Exception) {
            }
        }
    }
}
