package com.thinlineit.ctrlf.page.editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.entity.Page
import com.thinlineit.ctrlf.repository.dao.PageRepository
import com.thinlineit.ctrlf.util.Event
import com.thinlineit.ctrlf.util.Status
import kotlinx.coroutines.launch

class PageEditorViewModel(
    private val pageInfo: Page,
    private val topicTitle: String,
    private val topicId: Int
) : ViewModel() {
    private val contentRepository by lazy {
        PageRepository()
    }
    val content = MutableLiveData<String>(pageInfo.content)
    val pageTitle = MutableLiveData<String>(pageInfo.title)
    val topicTitleStr = MutableLiveData<String>(topicTitle)
    val topicIdInfo = MutableLiveData<Int>(topicId)
    val summary = MutableLiveData<String>()
    val createPageStatus = MutableLiveData<Event<Status>>()

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
                201 -> createPageStatus.value = Event(Status.SUCCESS)
                else -> createPageStatus.value = Event(Status.FAILURE)
            }
        }
    }
}
