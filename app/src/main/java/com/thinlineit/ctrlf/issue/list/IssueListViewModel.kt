package com.thinlineit.ctrlf.issue.list

import androidx.lifecycle.LiveData
<<<<<<< HEAD
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
=======
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thinlineit.ctrlf.issue.Issue

class IssueListViewModel : ViewModel() {
    private val _issueList = MutableLiveData<List<Issue>>(emptyList())
    val issueList: LiveData<List<Issue>>
>>>>>>> dev
        get() = _issueList

    init {
        loadIssue()
    }

    private fun loadIssue() {
<<<<<<< HEAD
        viewModelScope.loadingLaunch {
            try {
                _issueList.value = issueRepository.loadIssueList()
            } catch (e: Exception) {
            }
=======
        // TODO: Load the list of issue using "getIssue" api
        _issueList.value = createIssue()
    }

    private fun createIssue(): List<Issue> {
        val contentStr =
            "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz" +
                "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
        return (1..9).map { i ->
            if (i % 2 != 0) Issue(i, "title$i", 1, 1, "2021-07-12", contentStr)
            else Issue(i, "title$i", 1, 1, "2021-07-12", contentStr + contentStr)
>>>>>>> dev
        }
    }
}
