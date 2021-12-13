package com.thinlineit.ctrlf.page.editor

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
<<<<<<< HEAD
    private val pageInfo: Page,
    private val topicTitle: String,
    private val topicId: Int,
    private val mode: PageEditorActivity.Mode,
=======
    pageInfo: Page,
    topicTitle: String,
    topicId: Int,
>>>>>>> feature/editor
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

<<<<<<< HEAD
    private val _editPageStatus = MutableLiveData<Event<Status>>()
    val editPageStatus: LiveData<Event<Status>>
        get() = _editPageStatus
=======
    private val _createPageStatus = MutableLiveData<Event<Status>>()
    val createPageStatus: LiveData<Event<Status>>
        get() = _createPageStatus
    var toolboxController: ToolboxController? = null

    private val _url = MutableLiveData<String?>()
    val url: LiveData<String?>
        get() = _url

    private val _fileName = MutableLiveData<String?>()
    val fileName: LiveData<String?>
        get() = _fileName
>>>>>>> feature/editor

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
            EDIT -> _editPageStatus.value = Event(Status.SUCCESS)
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
