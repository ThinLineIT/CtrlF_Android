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

    private val curNoteId = MutableLiveData<Int>()
    private val curTopicId = MutableLiveData<Int>()
    private val curPageIdInfo = MutableLiveData<Pair<Int, Int>>()
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
    val page: LiveData<Page?> = curPageIdInfo.switchMap { pageId ->
        liveData {
            val page = if (pageId.first == UNSET_ID || pageId.second == UNSET_ID) null
            else {
                emit(loadPage())
                openRightPane()
            }
        }
    }

    private suspend fun loadPage(): Page? = withContext(Dispatchers.IO) {
        val pageId = curPageIdInfo.value?.first ?: return@withContext null
        val versionNo = curPageIdInfo.value?.second ?: return@withContext null
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
        curPageIdInfo.value = pageIdInfo
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

    fun createTopic(topicTitleEdit: String, reasonEdit: String) {
        val noteId = curNoteId.value ?: return

        viewModelScope.launch {
            if (topicTitleEdit != "" && reasonEdit != "") {
                pageRepository.createTopic(noteId, topicTitleEdit, reasonEdit)
            }
        }
        selectNote(noteId)
    }

    fun updateTopic(topicId: Int, newTopicTitle: String, reason: String) {
        val noteId = curNoteId.value ?: return
        viewModelScope.launch {
            pageRepository.updateTopic(topicId, newTopicTitle, reason)
        }
        selectNote(noteId)
    }

    fun updateNote(newNoteTitle: String, reason: String) {
        val noteId = curNoteId.value ?: return
        viewModelScope.launch {
            pageRepository.updateNote(noteId, newNoteTitle, reason)
        }
    }

    init {
        closeRightPane()
    }
}
