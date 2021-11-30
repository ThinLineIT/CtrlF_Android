package com.thinlineit.ctrlf.page.editor

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.entity.Page
import com.thinlineit.ctrlf.repository.dao.ContentRepository
import com.thinlineit.ctrlf.util.Event
import com.thinlineit.ctrlf.util.Status
import kotlinx.coroutines.launch

class PageEditorViewModel(
    pageInfo: Page,
    topicTitle: String,
    topicId: Int,
    private val contentRepository: ContentRepository = ContentRepository()
) : ViewModel() {

    val content = MutableLiveData<String>(pageInfo.content)
    val pageTitle = MutableLiveData<String>(pageInfo.title)
    val topicTitleStr = MutableLiveData<String>(topicTitle)
    val topicIdInfo = MutableLiveData<Int>(topicId)
    val summary = MutableLiveData<String>()

    private val _createPageStatus = MutableLiveData<Event<Status>>()
    val createPageStatus: LiveData<Event<Status>>
        get() = _createPageStatus
    var toolboxController: ToolboxController? = null

    private val _dropUrl = MutableLiveData<String?>()
    val dropUrl: LiveData<String?>
        get() = _dropUrl

    private val _attachUrl = MutableLiveData<String?>()
    val attachUrl: LiveData<String?>
        get() = _attachUrl

    fun complete() {
        val topicId = topicIdInfo.value ?: return
        val pageTitle = pageTitle.value ?: return
        val content = content.value ?: return
        val summary = summary.value ?: return

        // TODO: if it's true, inform success
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
    }

    private fun addImageUrl(url: String, type: String) {
        when (type) {
            DROP_IMAGE_TYPE -> {
                _dropUrl.value = url
                _dropUrl.value = null
            }
            ATTACH_IMAGE_TYPE -> {
                _attachUrl.value = url
                _attachUrl.value = null
            }
            else -> return
        }
    }

    fun loadImageUrl(uri: Uri, type: String) {
        viewModelScope.launch {
            try {
                addImageUrl(contentRepository.uploadImage(uri), type)
            } catch (e: Exception) {
                return@launch
            }
        }
    }

    companion object {
        const val DROP_IMAGE_TYPE = "dropImageType"
        const val ATTACH_IMAGE_TYPE = "attachImageType"
    }
}
