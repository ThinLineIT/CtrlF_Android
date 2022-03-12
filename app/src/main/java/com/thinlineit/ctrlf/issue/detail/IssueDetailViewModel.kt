package com.thinlineit.ctrlf.issue.detail

import android.util.Log
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

    private val _issueRejectStatus = MutableLiveData<Event<Status>>()
    val issueRejectStatus: LiveData<Event<Status>>
        get() = _issueRejectStatus

    private val _issueCloseStatus = MutableLiveData<Event<Status>>()
    val issueCloseStatus: LiveData<Event<Status>>
        get() = _issueCloseStatus

    private val _issueDeleteStatus = MutableLiveData<Event<Status>>()
    val issueDeleteStatus: LiveData<Event<Status>>
        get() = _issueDeleteStatus

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

    fun rejectIssue() {
        viewModelScope.launch {
            if (issueId.value != null) {
                when (issueRepository.rejectIssue(issueId.value!!.toInt())) {
                    200 -> _issueRejectStatus.value = Event(Status.SUCCESS)
                    else -> _issueRejectStatus.value = Event(Status.FAILURE)
                }
            }
        }
    }

    fun closeIssue() {
        viewModelScope.launch {
            if (issueId.value != null) {
                val code = issueRepository.closeIssue(issueId.value!!.toInt())
                Log.d(TAG, "closeIssue : $code")
                when (code) {
                    200 -> _issueCloseStatus.value = Event(Status.SUCCESS)
                    else -> _issueCloseStatus.value = Event(Status.FAILURE)
                }
            }
        }
    }

    fun deleteIssue() {
        viewModelScope.launch {
            if (issueId.value != null) {
                val code = issueRepository.deleteIssue(issueId.value!!.toInt())
                Log.d(TAG, "closeIssue : $code")
                when (code) {
                    200 -> _issueDeleteStatus.value = Event(Status.SUCCESS)
                    else -> _issueDeleteStatus.value = Event(Status.FAILURE)
                }
            }
        }
    }

    private fun initToolbarTitle() {
        if (issue.value?.relatedModelType.isNullOrEmpty() || issue.value?.action.isNullOrEmpty())
            return

        val modelType = identifyModelType(issue.value!!.relatedModelType)
        val modelAction = identifyModelAction(issue.value!!.action)

        _toolbarTitle.value =
            when (modelType) {
                ModelType.Note -> when (modelAction) {
                    ModelAction.Create -> R.string.label_create_note
                    ModelAction.Update -> R.string.label_update_note
                    ModelAction.Delete -> R.string.label_delete_note
                }
                ModelType.Topic -> when (modelAction) {
                    ModelAction.Create -> R.string.label_create_topic
                    ModelAction.Update -> R.string.label_update_topic
                    ModelAction.Delete -> R.string.label_delete_topic
                }
                ModelType.Page -> when (modelAction) {
                    ModelAction.Create -> R.string.label_create_page
                    ModelAction.Update -> R.string.label_update_page
                    ModelAction.Delete -> R.string.label_delete_page
                }
            }
    }

    private fun initButtonVisible(contentType: String, status: String) {
        _detailButtonStatus.value = identifyModelType(contentType) == ModelType.Page
        _approveButtonStatus.value = status == REQUESTED
    }

    private fun identifyModelType(modelType: String): ModelType {
        val type = when (modelType) {
            "NOTE" -> ModelType.Note
            "TOPIC" -> ModelType.Topic
            else -> ModelType.Page
        }
        return type
    }

    private fun identifyModelAction(modelAction: String): ModelAction {
        val action = when (modelAction) {
            "CREATE" -> ModelAction.Create
            "UPDATE" -> ModelAction.Update
            else -> ModelAction.Delete
        }
        return action
    }

    companion object {
        const val REQUESTED = "REQUESTED"
        const val TAG = "IssueDetailViewModel"
    }
}

sealed class ModelType() {
    object Note : ModelType()
    object Topic : ModelType()
    object Page : ModelType()
}

sealed class ModelAction() {
    object Create : ModelAction()
    object Update : ModelAction()
    object Delete : ModelAction()
}
