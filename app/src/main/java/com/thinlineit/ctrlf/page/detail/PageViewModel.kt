package com.thinlineit.ctrlf.page.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.entity.Note
import com.thinlineit.ctrlf.entity.Page
import com.thinlineit.ctrlf.entity.Topic
import com.thinlineit.ctrlf.entity.UNSET_ID
import com.thinlineit.ctrlf.repository.dao.ContentRepository
import com.thinlineit.ctrlf.util.addSourceList
import com.thinlineit.ctrlf.util.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PageViewModel(
    private val pageRepository: ContentRepository = ContentRepository()
) : BaseViewModel() {
    val curNoteId = MutableLiveData<Int>()
    private val curTopicId = MutableLiveData<Int>()
    private val curPageIdAndVersionNo = MutableLiveData<Pair<Int, Int>>()
    private val _isRightPaneOpen = MutableLiveData<Boolean>(false)
    private val _isFabOpen = MutableLiveData<Boolean>(false)

    val note: LiveData<Note?> = curNoteId.switchMap { noteId ->
        liveData {
            val note = if (noteId == UNSET_ID) null else pageRepository.loadNote(noteId)
            emit(note)
        }
    }
    val topicList: LiveData<List<Topic>?> = curNoteId.switchMap { noteId ->
        liveData {
            val topicList = if (noteId == UNSET_ID) null else pageRepository.loadTopicList(noteId)
            emit(topicList)
        }
    }
    val topic: LiveData<Topic?> = MediatorLiveData<Topic?>().apply {
        addSourceList(topicList, curTopicId) {
            value = topicList.value?.find { it.id == curTopicId.value }
        }
    }
    val pageList: LiveData<List<Page>?> = curTopicId.switchMap { topicId ->
        liveData {
            val pageList = if (topicId == UNSET_ID) null else pageRepository.loadPageList(topicId)
            emit(pageList)
        }
    }
    val page: LiveData<Page?> = curPageIdAndVersionNo.switchMap { pageIdAndVersionNo ->
        liveData {
            val pageId = pageIdAndVersionNo.first
            val versionNo = pageIdAndVersionNo.second
            val page = loadPage(pageId, versionNo)
            if (page != null) {
                emit(loadPage(pageId, versionNo))
                openRightPane()
            }
        }
    }

    private suspend fun loadPage(pageId: Int, versionNo: Int): Page? = withContext(Dispatchers.IO) {
        if (pageId == UNSET_ID || versionNo == UNSET_ID) return@withContext null
        pageRepository.loadPage(
            pageId,
            versionNo
        )
    }

    val isRightPaneOpen: LiveData<Boolean>
        get() = _isRightPaneOpen
    val isFabOpen: LiveData<Boolean>
        get() = _isFabOpen

    fun setPageHierarchy(newNoteId: Int, newTopicId: Int, newPageIdInfo: Pair<Int, Int>) {
        selectNote(newNoteId)
        selectTopic(newTopicId)
        selectPage(newPageIdInfo)
    }

    fun selectNote(noteId: Int) {
        curNoteId.value = noteId
    }

    fun selectTopic(topicId: Int) {
        curTopicId.value = topicId
    }

    fun selectPage(pageIdInfo: Pair<Int, Int>) {
        curPageIdAndVersionNo.value = pageIdInfo
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

    fun onBackPressed(): Boolean {
        return when {
            isRightPaneOpen.value == true -> {
                closeRightPane()
                true
            }
            curTopicId.value != UNSET_ID -> {
                selectPage(Pair(UNSET_ID, UNSET_ID))
                selectTopic(UNSET_ID)
                true
            }
            else -> {
                false
            }
        }
    }

    suspend fun createTopic(topicTitleEdit: String, reasonEdit: String): Boolean {
        val noteId = curNoteId.value
        viewModelScope.launch {
            if (topicTitleEdit != "" && reasonEdit != "") {
                pageRepository.createTopic(noteId ?: return@launch, topicTitleEdit, reasonEdit)
                selectNote(noteId)
            }
        }
        return true
    }

    fun updateTopic(topicId: Int, newTopicTitle: String, reason: String): Boolean {
        val noteId = curNoteId.value

        viewModelScope.launch {
            pageRepository.updateTopic(topicId, newTopicTitle, reason)
            selectNote(noteId ?: return@launch)
        }
        return true
    }

    fun updateNote(newNoteTitle: String, reason: String): Boolean {
        val noteId = curNoteId.value

        viewModelScope.launch {
            pageRepository.updateNote(noteId ?: return@launch, newNoteTitle, reason)
        }
        return true
    }

    suspend fun deletePage(reason: String, pageId: Int): Boolean {
        if (!pageRepository.deletePage(pageId, reason)) return false
        return true
    }

    suspend fun deleteTopic(reason: String, topicId: Int): Boolean {
        if (!pageRepository.deleteTopic(topicId, reason)) return false
        return true
    }

    suspend fun deleteNote(reason: String, noteId: Int): Boolean {
        if (!pageRepository.deleteNote(noteId, reason)) return false
        return true
    }

    init {
        closeRightPane()
    }
}
