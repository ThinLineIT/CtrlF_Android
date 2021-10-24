package com.thinlineit.ctrlf.issue.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thinlineit.ctrlf.issue.Issue

class IssueListViewModel : ViewModel() {
    private val _issueList = MutableLiveData<List<Issue>>(emptyList())
    val issueList: LiveData<List<Issue>>
        get() = _issueList

    init {
        loadIssue()
    }

    private fun loadIssue() {
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
        }
    }
}
