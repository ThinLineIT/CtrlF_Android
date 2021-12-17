package com.thinlineit.ctrlf.issue.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.entity.Issue
import com.thinlineit.ctrlf.repository.dao.IssueRepository
import com.thinlineit.ctrlf.util.Event
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.base.BaseViewModel
import kotlinx.coroutines.launch

class IssueDetailViewModel(
    issueId: Int,
    private val issueRepository: IssueRepository = IssueRepository()
) : BaseViewModel() {

    private val _issueId = MutableLiveData<Int>(issueId)
    val issueId: LiveData<Int>
        get() = _issueId

    private val _issue = MutableLiveData<Issue>()
    val issue: LiveData<Issue>
        get() = _issue

    private val _toolbarTitle = MutableLiveData<Int>(R.string.label_create_page)
    val toolbarTitle: LiveData<Int>
        get() = _toolbarTitle

    private val _issueApproveStatus = MutableLiveData<Event<Status>>()
    val issueApproveStatus: LiveData<Event<Status>>
        get() = _issueApproveStatus

    private val _detailButtonStatus = MutableLiveData<Boolean>(false)
    val detailButtonStatus: LiveData<Boolean>
        get() = _detailButtonStatus

    private val _approveButtonStatus = MutableLiveData<Boolean>(false)
    val approveButtonStatus: LiveData<Boolean>
        get() = _approveButtonStatus

    init {
        loadIssue(issueId)
    }

    private fun loadIssue(issueId: Int) {
        viewModelScope.loadingLaunch {
            try {
                _issue.value = issueRepository.getIssueDetail(issueId.toString())
                initToolbarTitle()
                initButtonVisible(issue.value?.relatedModelType ?: "", issue.value?.status ?: "")
            } catch (e: Exception) {
            }
        }
    }

    fun approveIssue() {
        viewModelScope.launch {
            if (issueId.value != null) {
                when (issueRepository.approveIssue(issueId.value!!.toInt())) {
                    200 -> _issueApproveStatus.value = Event(Status.SUCCESS)
                    else -> _issueApproveStatus.value = Event(Status.FAILURE)
                }
            }
        }
    }

    private fun initToolbarTitle() {
        _toolbarTitle.value =
            when (issue.value?.relatedModelType ?: NULL) {
                NOTE -> when (issue.value?.action) {
                    CREATE -> R.string.label_create_note
                    UPDATE -> R.string.label_update_note
                    DELETE -> R.string.label_delete_note
                    else -> R.string.empty_text
                }
                TOPIC -> when (issue.value?.action) {
                    CREATE -> R.string.label_create_topic
                    UPDATE -> R.string.label_update_topic
                    DELETE -> R.string.label_delete_topic
                    else -> R.string.empty_text
                }
                PAGE -> when (issue.value?.action) {
                    CREATE -> R.string.label_create_page
                    UPDATE -> R.string.label_update_page
                    DELETE -> R.string.label_delete_page
                    else -> R.string.empty_text
                }
                else -> R.string.empty_text
            }
    }

    private fun initButtonVisible(contentType: String, status: String) {
        _detailButtonStatus.value = contentType == PAGE
        _approveButtonStatus.value = status == REQUESTED
    }

    companion object {
        const val REQUESTED = "REQUESTED"
        const val NOTE = "NOTE"
        const val TOPIC = "TOPIC"
        const val PAGE = "PAGE"
        const val NULL = "null"
        const val CREATE = "CREATE"
        const val UPDATE = "UPDATE"
        const val DELETE = "DELETE"
    }
}
