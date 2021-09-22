package com.thinlineit.ctrlf.page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.notes.NoteDao
import com.thinlineit.ctrlf.notes.TopicDao
import com.thinlineit.ctrlf.repository.PageRepository
import kotlinx.coroutines.launch

class PageViewModel(noteId: Int) : ViewModel() {

    private val pageRepository: PageRepository by lazy {
        PageRepository()
    }
    private val noteIdString = MutableLiveData(noteId.toString())
    private val pageInfo = MutableLiveData<PageDao>()
    private val noteDetailInfo = MutableLiveData<NoteDao>()

    private val _openSlidingPane = MutableLiveData<Boolean>()
    val openSlidingPane: LiveData<Boolean>
        get() = _openSlidingPane

    val noteInfo = MutableLiveData<List<TopicDao>>(listOf())
    val topicInfo = MutableLiveData<List<PageDao>>()
    val content = Transformations.map(pageInfo) { it.content }
    val noteDetailTitle = Transformations.map(noteDetailInfo) { it.title }

    lateinit var topicDetailTitle: String

    init {
        loadPage(1)
        loadNoteInfo()
        loadNoteDetailInfo()
        _openSlidingPane.value = false
    }

    fun openPage(pageId: Int) {
        loadPage(pageId)
    }

    private fun loadPage(pageId: Int) {
        viewModelScope.launch {
            pageInfo.setValue(pageRepository.loadPage(pageId))
        }
    }

    private fun loadNoteInfo() {
        viewModelScope.launch {
            val noteId = noteIdString.value ?: return@launch
            noteInfo.setValue(pageRepository.loadNoteInfo(noteId))
        }
    }

    private fun loadNoteDetailInfo() {
        viewModelScope.launch {
            val noteId = noteIdString.value ?: return@launch
            noteDetailInfo.setValue(pageRepository.loadNoteDetailInfo(noteId))
        }
    }

    fun closeSliding() {
        _openSlidingPane.value = false
    }

    fun selectTopic(topicId: Int, topicTitle: String) {
        loadPageList(topicId)
        topicDetailTitle = topicTitle
    }

    private fun loadPageList(topicId: Int) {
        viewModelScope.launch {
            topicInfo.setValue(pageRepository.loadPageList(topicId))
        }
    }

    fun openSliding() {
        _openSlidingPane.value = true
    }
}
