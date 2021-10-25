package com.thinlineit.ctrlf.page.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.entity.Note
import com.thinlineit.ctrlf.entity.Page
import com.thinlineit.ctrlf.entity.Topic
import com.thinlineit.ctrlf.repository.dao.PageRepository
import com.thinlineit.ctrlf.util.base.BaseViewModel

class PageViewModel(
    private val pageRepository: PageRepository = PageRepository()
) : BaseViewModel() {
    private val _note = MutableLiveData<Note>()
    private val _topic = MutableLiveData<Topic>()
    private val _page = MutableLiveData<Page>()
    private val _isRightPaneOpen = MutableLiveData<Boolean>(false)
    private val _isFabOpen = MutableLiveData<Boolean>(false)

    val note: LiveData<Note>
        get() = _note
    val topic: LiveData<Topic>
        get() = _topic
    val page: LiveData<Page>
        get() = _page
    val topicList = Transformations.map(note) { it.topicList }
    val pageList = Transformations.map(topic) { it.pageList }

    val isRightPaneOpen: LiveData<Boolean>
        get() = _isRightPaneOpen
    val isFabOpen: LiveData<Boolean>
        get() = _isFabOpen

    init {
        closeRightPane()
    }

    fun loadNote(noteId: Int) {
        viewModelScope.loadingLaunch {
            try {
                _note.value = pageRepository.loadNote(noteId)
                loadTopicList(noteId)
            } catch (e: Exception) {
            }
        }
    }

    fun selectTopic(topic: Topic) {
        _topic.value = topic
        loadPageList(topic.id)
    }

    fun selectPage(pageId: Int) {
        loadPage(pageId)
    }

    fun openRightPane() {
        _isRightPaneOpen.value = true
    }

    fun closeRightPane() {
        _isRightPaneOpen.value = false
    }

    fun toggleFab() {
        _isFabOpen.value = _isFabOpen.value?.not() ?: true
    }

    private fun loadTopicList(noteId: Int) {
        viewModelScope.loadingLaunch {
            try {
                val topicList = pageRepository.loadTopicList(noteId)
                _note.value = note.value?.copy(topicList = topicList)
            } catch (e: Exception) {
            }
        }
    }

    private fun loadPageList(topicId: Int) {
        viewModelScope.loadingLaunch {
            try {
                val pageList = pageRepository.loadPageList(topicId)
                _topic.value = topic.value?.copy(pageList = pageList)
            } catch (e: Exception) {
            }
        }
    }

    private fun loadPage(pageId: Int) {
        viewModelScope.loadingLaunch {
            try {
                _page.value = pageRepository.loadPage(pageId)
                openRightPane()
            } catch (e: Exception) {
            }
        }
    }
}
