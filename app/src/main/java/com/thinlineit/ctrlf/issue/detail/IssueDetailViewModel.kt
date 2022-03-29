package com.thinlineit.ctrlf.issue.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.entity.Issue
import com.thinlineit.ctrlf.entity.Page
import com.thinlineit.ctrlf.entity.Topic
import com.thinlineit.ctrlf.entity.UNSET_ID
import com.thinlineit.ctrlf.repository.dao.ContentRepository
import com.thinlineit.ctrlf.repository.dao.IssueRepository
import com.thinlineit.ctrlf.util.Event
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.base.BaseViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IssueDetailViewModel(
    issueId: Int,
    private val issueRepository: IssueRepository = IssueRepository(),
    private val contentRepository: ContentRepository = ContentRepository()
) : BaseViewModel() {

    private val _issueId = MutableLiveData<Int>(issueId)
    val issueId: LiveData<Int>
        get() = _issueId

    private val _issue = MutableLiveData<Issue>()
    val issue: LiveData<Issue>
        get() = _issue

    private val _pageInfo = MutableLiveData<Page>()
    val pageInfo: LiveData<Page>
        get() = _pageInfo

    private val _topicInfo = MutableLiveData<Topic>()
    val topicInfo: LiveData<Topic>
        get() = _topicInfo

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

    private val _issueInfoStatus = MutableLiveData<Boolean>(true)
    val issueInfoStatus: LiveData<Boolean>
        get() = _issueInfoStatus

    private val _issuePermissionStatus = MutableLiveData<Boolean>(false)
    val issuePermissionStatus: LiveData<Boolean>
        get() = _issuePermissionStatus

    private val _detailButtonStatus = MutableLiveData<Boolean>(false)
    val detailButtonStatus: LiveData<Boolean>
        get() = _detailButtonStatus

    private val _approveButtonStatus = MutableLiveData<Boolean>(false)
    val approveButtonStatus: LiveData<Boolean>
        get() = _approveButtonStatus

    val issueTitle = MutableLiveData("")

    init {
        loadIssue()
    }

    fun loadIssue() {
        viewModelScope.loadingLaunch {
            try {
                _issue.value = issueRepository.getIssueDetail(issueId.value.toString())
                issueTitle.postValue(issue.value?.title ?: "")
                initToolbarTitle()
                initButtonVisible(issue.value?.relatedModelType ?: "", issue.value?.status ?: "")
                checkPermissionIssue()
                getPageInfo(issue.value?.pageId, issue.value?.versionNo)
                checkIssueInfo()
            } catch (e: Exception) {
            }
        }
    }

    fun approveIssue() {
        viewModelScope.launch {
            if (issueId.value != null) {
                when (issueRepository.approveIssue(issueId.value ?: return@launch)) {
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

    suspend fun updateIssue(
        newTitle: String?,
        reason: String
    ): Boolean {
        return withContext(viewModelScope.coroutineContext) {
            if (issueId.value != null)
                if (newTitle == "" || reason == "") return@withContext false
            if (!issueRepository.updateIssue(
                    issueId.value!!,
                    newTitle,
                    null,
                    reason
                )
            ) return@withContext false
            loadIssue()
            return@withContext true
        }
    }

    fun checkPermissionIssue() {
        viewModelScope.launch {
            if (issueId.value != null) {
                when (issueRepository.checkIssuePermission(issueId.value!!)) {
                    true -> _issuePermissionStatus.value = true
                    false -> _issuePermissionStatus.value = false
                }
            }
        }
    }

    private fun getPageInfo(pageId: Int?, versionNo: Int?) {
        viewModelScope.launch {
            if (pageId == UNSET_ID || versionNo == UNSET_ID) return@launch
            if (pageId == null || versionNo == null) return@launch
            _pageInfo.value = contentRepository.loadPage(
                pageId,
                versionNo
            )
            getTopicInfo(pageInfo.value?.topicId)
        }
    }

    private fun getTopicInfo(topicId: Int?) {
        viewModelScope.launch {
            if (topicId == null) return@launch
            _topicInfo.value = contentRepository.loadTopic(topicId)
        }
    }

    private fun initToolbarTitle() {
        val modelType = ModelType.getModelType(issue.value?.relatedModelType ?: return) ?: return
        val modelAction = ModelAction.getModelAction(issue.value?.action ?: return) ?: return

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

    private fun checkIssueInfo() {
        if (issue.value == null ||
            issue.value?.action == null ||
            issue.value?.relatedModelType == null ||
            issue.value?.id == null || issue.value?.title == null ||
            issue.value?.reason == null
        )
            _issueInfoStatus.value = false
    }

    private fun initButtonVisible(contentType: String, status: String) {
        _detailButtonStatus.value = ModelType.getModelType(contentType) == ModelType.Page
        _approveButtonStatus.value = status == REQUESTED
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

    companion object {
        fun getModelType(modelType: String): ModelType? = when (modelType) {
            "NOTE" -> ModelType.Note
            "TOPIC" -> ModelType.Topic
            "PAGE" -> ModelType.Page
            else -> null
        }
    }
}

sealed class ModelAction() {
    object Create : ModelAction()
    object Update : ModelAction()
    object Delete : ModelAction()

    companion object {
        fun getModelAction(modelAction: String): ModelAction? = when (modelAction) {
            "CREATE" -> ModelAction.Create
            "UPDATE" -> ModelAction.Update
            "DELETE" -> ModelAction.Delete
            else -> null
        }
    }
}
