package com.thinlineit.ctrlf.page.editor

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.entity.Page
import com.thinlineit.ctrlf.page.editor.PageEditorActivity.Mode.CREATE
import com.thinlineit.ctrlf.page.editor.PageEditorActivity.Mode.EDIT
import com.thinlineit.ctrlf.page.editor.PageEditorActivity.Mode.UPDATE
import com.thinlineit.ctrlf.repository.dao.ContentRepository
import com.thinlineit.ctrlf.repository.dao.IssueRepository
import com.thinlineit.ctrlf.util.Event
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.base.BaseViewModel
import kotlinx.coroutines.launch

class PageEditorViewModel(
    private val pageInfo: Page,
    private val topicTitle: String,
    private val topicId: Int,
    private val issueSummary: String,
    private val mode: PageEditorActivity.Mode,
    private val contentRepository: ContentRepository = ContentRepository(),
    private val issueRepository: IssueRepository = IssueRepository()
) : BaseViewModel() {

    val content = MutableLiveData<String>(pageInfo.content)
    val pageTitle = MutableLiveData<String>(pageInfo.title)
    val topicTitleStr = MutableLiveData<String>(topicTitle)
    val topicIdInfo = MutableLiveData<Int>(topicId)
    val summary = MutableLiveData<String>(issueSummary)
    val headerText = when (mode) {
        CREATE -> R.string.label_add_page
        EDIT -> R.string.label_edit_page
        UPDATE -> R.string.label_update_issue_page
    }

    val summaryHint = when (mode) {
        CREATE -> R.string.label_summary
        EDIT -> R.string.label_reason_for_revision
        UPDATE -> R.string.empty_text
    }

    private val _editPageStatus = MutableLiveData<Event<Status>>()
    val editPageStatus: LiveData<Event<Status>>
        get() = _editPageStatus

    var toolboxController: ToolboxController? = null

    private val _url = MutableLiveData<String?>()
    val url: LiveData<String?>
        get() = _url

    private val _fileName = MutableLiveData<String?>()
    val fileName: LiveData<String?>
        get() = _fileName

    fun complete() {
        val topicId = topicIdInfo.value ?: return
        val pageTitle = pageTitle.value ?: return
        val content = content.value ?: return
        val summary = summary.value ?: return

        when (mode) {
            CREATE ->
                viewModelScope.launch {
                    when (
                        contentRepository.createPage(
                            topicId,
                            pageTitle,
                            content,
                            summary
                        )
                    ) {
                        201 -> _editPageStatus.value = Event(Status.SUCCESS)
                        else -> _editPageStatus.value = Event(Status.FAILURE)
                    }
                }
            EDIT ->
                viewModelScope.launch {
                    _editPageStatus.value = if (
                        contentRepository.updatePage(
                            pageInfo.id,
                            pageTitle,
                            content,
                            summary
                        )
                    ) Event(Status.SUCCESS)
                    else Event(Status.FAILURE)
                }

            UPDATE ->
                viewModelScope.launch {
                    val issueId = pageInfo.issueId ?: return@launch
                    _editPageStatus.value = if (
                        issueRepository.updateIssue(
                            issueId,
                            pageTitle,
                            content,
                            summary
                        )
                    ) Event(Status.SUCCESS)
                    else Event(Status.FAILURE)
                }
        }
    }

    private fun addImageUrl(url: String, name: String) {
        _fileName.value = name
        _url.value = url
        _url.value = null
    }

    fun loadImageUrl(uri: Uri, name: String) {
        viewModelScope.launch {
            try {
                addImageUrl(contentRepository.uploadImage(uri, name), name)
            } catch (e: Exception) {
                return@launch
            }
        }
    }
}
