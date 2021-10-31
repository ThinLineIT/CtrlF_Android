package com.thinlineit.ctrlf.issue.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.entity.Issue
import com.thinlineit.ctrlf.entity.UNSET_ID
import com.thinlineit.ctrlf.repository.dao.IssueRepository
import com.thinlineit.ctrlf.util.base.BaseViewModel
import kotlinx.coroutines.launch

class IssueDetailViewModel(issueId: Int) : BaseViewModel() {
    private val issueRepository by lazy {
        IssueRepository()
    }

    private val _issueId = MutableLiveData<Int>(issueId)
    val issueId: LiveData<Int>
        get() = _issueId

    private val _issue = MutableLiveData<Issue>()
    val issue: LiveData<Issue>
        get() = _issue

    private val _toolbarTitle = MutableLiveData<String>()
    val toolbarTitle: LiveData<String>
        get() = _toolbarTitle

    init {
        initToolbarTitle()
        loadIssue(issueId)
    }

    fun loadIssue(issueId: Int) {
        viewModelScope.loadingLaunch {
            try {
                _issue.value = issueRepository.getIssueDetail(issueId.toString())
            } catch (e: Exception) {
            }
        }
    }

    fun approveIssue() {
        viewModelScope.launch {
            if (issueId.value != null) {
                val message = issueRepository.approveIssue(issueId.value!!.toInt())
            }
        }
    }

    fun issueNullCheck(topicId: Int, pageId: Int): String {
        return if (pageId != UNSET_ID)
            PAGE
        else if (topicId != UNSET_ID)
            TOPIC
        else
            NOTE
    }

    fun initToolbarTitle() {
        _toolbarTitle.value = CREATE +
            issueNullCheck(
                issue.value?.topicId ?: UNSET_ID,
                issue.value?.pageId ?: UNSET_ID
            )
    }

    companion object {
        const val NOTE = "Note"
        const val TOPIC = " Topic"
        const val PAGE = "Page"
        const val CREATE = "Create"
    }
}
