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

    val issueTitle = MutableLiveData("")

    init {
        loadIssue(issueId)
    }

    private fun loadIssue(issueId: Int) {
        viewModelScope.loadingLaunch {
            try {
                _issue.value = issueRepository.getIssueDetail(issueId.toString())
                issueTitle.postValue(issue.value?.title ?: "")
                initToolbarTitle()
                initButtonVisible(issue.value?.relatedModelType ?: "", issue.value?.status ?: "")
            } catch (e: Exception) {
            }
        }
    }

    fun approveIssue() {
        viewModelScope.launch {
            if (issueId.value != null) {
                when (issueRepository.approveIssue(issueId.value ?: return@launch.toInt())) {
                    true -> _issueApproveStatus.value = Event(Status.SUCCESS)
                    else -> _issueApproveStatus.value = Event(Status.FAILURE)
                }
            }
        }
    }

    private fun initToolbarTitle() {
        _toolbarTitle.value =
            when (issue.value?.relatedModelType) {
                ModelType.NOTE.name -> when (issue.value?.action) {
                    ModelAction.CREATE.name -> R.string.label_create_note
                    ModelAction.UPDATE.name -> R.string.label_update_note
                    ModelAction.DELETE.name -> R.string.label_delete_note
                    else -> R.string.empty_text
                }
                ModelType.TOPIC.name -> when (issue.value?.action) {
                    ModelAction.CREATE.name -> R.string.label_create_topic
                    ModelAction.UPDATE.name -> R.string.label_update_topic
                    ModelAction.DELETE.name -> R.string.label_delete_topic
                    else -> R.string.empty_text
                }
                ModelType.PAGE.name -> when (issue.value?.action) {
                    ModelAction.CREATE.name -> R.string.label_create_page
                    ModelAction.UPDATE.name -> R.string.label_update_page
                    ModelAction.DELETE.name -> R.string.label_delete_page
                    else -> R.string.empty_text
                }
                else -> R.string.empty_text
            }
    }

    private fun initButtonVisible(contentType: String, status: String) {
        _detailButtonStatus.value = contentType == ModelType.PAGE.name
        _approveButtonStatus.value = status == REQUESTED
    }

    companion object {
        const val REQUESTED = "REQUESTED"
    }
}

enum class ModelType {
    NOTE,
    TOPIC,
    PAGE
}

enum class ModelAction {
    CREATE,
    UPDATE,
    DELETE
}
