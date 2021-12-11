package com.thinlineit.ctrlf.page.editor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.entity.Page
import com.thinlineit.ctrlf.page.editor.PageEditorActivity.Mode.CREATE
import com.thinlineit.ctrlf.page.editor.PageEditorActivity.Mode.EDIT
import com.thinlineit.ctrlf.repository.dao.ContentRepository
import com.thinlineit.ctrlf.util.Event
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.base.BaseViewModel
import kotlinx.coroutines.launch

class PageEditorViewModel(
    private val pageInfo: Page,
    private val topicTitle: String,
    private val topicId: Int,
    private val mode: PageEditorActivity.Mode,
    private val contentRepository: ContentRepository = ContentRepository()
) : BaseViewModel() {

    val content = MutableLiveData<String>(pageInfo.content)
    val pageTitle = MutableLiveData<String>(pageInfo.title)
    val topicTitleStr = MutableLiveData<String>(topicTitle)
    val topicIdInfo = MutableLiveData<Int>(topicId)
    val summary = MutableLiveData<String>()
    val headerText = when (mode) {
        CREATE -> R.string.label_add_page
        EDIT -> R.string.label_edit_page
    }

    val summaryHint = when (mode) {
        CREATE -> R.string.label_summary
        EDIT -> R.string.label_reason_for_revision
    }

    private val _createPageStatus = MutableLiveData<Event<Status>>()
    val createPageStatus: LiveData<Event<Status>>
        get() = _createPageStatus

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
                        201 -> _createPageStatus.value = Event(Status.SUCCESS)
                        else -> _createPageStatus.value = Event(Status.FAILURE)
                    }
                }
            EDIT -> TODO()
        }
    }
}
