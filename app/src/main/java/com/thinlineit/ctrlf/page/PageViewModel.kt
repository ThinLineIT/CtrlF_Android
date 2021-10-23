package com.thinlineit.ctrlf.page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.notes.NoteDao
import com.thinlineit.ctrlf.notes.TopicDao
import com.thinlineit.ctrlf.repository.PageRepository
import com.thinlineit.ctrlf.util.base.BaseViewModel

class PageViewModel(noteId: Int) : BaseViewModel() {

    private val pageRepository: PageRepository by lazy {
        PageRepository()
    }
    private val noteIdString = MutableLiveData(noteId.toString())
    private val pageInfo = MutableLiveData<PageDao>()
    private val noteDetailInfo = MutableLiveData<NoteDao>()

    private val _openSlidingPane = MutableLiveData<Boolean>()
    val openSlidingPane: LiveData<Boolean>
        get() = _openSlidingPane

    private val _isFabOpen = MutableLiveData<Boolean>(false)
    val isFabOpen: LiveData<Boolean>
        get() = _isFabOpen

    val noteInfo = MutableLiveData<List<TopicDao>>(listOf())
    val topicInfo = MutableLiveData<List<PageDao>>()
    val content = Transformations.map(pageInfo) { it.content }
    val pageTitle = Transformations.map(pageInfo) { it.title }
    val topicTitleTop = MutableLiveData<String>()
    val noteDetailTitle = Transformations.map(noteDetailInfo) { it.title }

    lateinit var topicDetailTitle: String

    init {
        loadNoteInfo()
        loadNoteDetailInfo()
        _openSlidingPane.value = false
    }

    fun openPage(pageId: Int) {
        loadPage(pageId)
    }

    private fun loadPage(pageId: Int) {
        viewModelScope.loadingLaunch {
            try {
                pageInfo.setValue(pageRepository.loadPage(pageId))
            } catch (e: Exception) {
            }
        }
    }

    private fun loadNoteInfo() {
        viewModelScope.loadingLaunch {
            try {
                val noteId = noteIdString.value ?: return@loadingLaunch
                noteInfo.setValue(pageRepository.loadNoteInfo(noteId))
            } catch (e: Exception) {
            }
        }
    }

    private fun loadNoteDetailInfo() {
        viewModelScope.loadingLaunch {
            try {
                val noteId = noteIdString.value ?: return@loadingLaunch
                noteDetailInfo.setValue(
                    pageRepository.loadNoteDetailInfo(noteId)
                )
            } catch (e: Exception) {
            }
        }
    }

    fun closeSliding() {
        _openSlidingPane.value = false
    }

    fun selectTopic(topicId: Int, topicTitle: String) {
        loadPageList(topicId)
        topicDetailTitle = topicTitle
        topicTitleTop.value = topicTitle
    }

    private fun loadPageList(topicId: Int) {
        viewModelScope.loadingLaunch {
            try {
                topicInfo.setValue(pageRepository.loadPageList(topicId))
            } catch (e: Exception) {
            }
        }
    }

    fun openSliding() {
        _openSlidingPane.value = true

    fun toggleFab() {
        _isFabOpen.value = _isFabOpen.value?.not() ?: true
    }
}
