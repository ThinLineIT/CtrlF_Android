package com.thinlineit.ctrlf.page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.notes.NoteDao
import com.thinlineit.ctrlf.notes.TopicDao
import com.thinlineit.ctrlf.repository.PageRepository
<<<<<<< HEAD
import com.thinlineit.ctrlf.repository.network.ContentService
=======
>>>>>>> dev
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
<<<<<<< HEAD
                pageInfo.setValue(ContentService.retrofitService.getPage(pageId.toString()))
=======
                pageInfo.setValue(pageRepository.loadPage(pageId))
>>>>>>> dev
            } catch (e: Exception) {
            }
        }
    }

    private fun loadNoteInfo() {
        viewModelScope.loadingLaunch {
            try {
                val noteId = noteIdString.value ?: return@loadingLaunch
<<<<<<< HEAD
                noteInfo.setValue(ContentService.retrofitService.getNote(noteId))
=======
                noteInfo.setValue(pageRepository.loadNoteInfo(noteId))
>>>>>>> dev
            } catch (e: Exception) {
            }
        }
    }

    private fun loadNoteDetailInfo() {
        viewModelScope.loadingLaunch {
            try {
                val noteId = noteIdString.value ?: return@loadingLaunch
                noteDetailInfo.setValue(
<<<<<<< HEAD
                    ContentService.retrofitService.getNoteDetail(Integer.parseInt(noteId))
=======
                    pageRepository.loadNoteDetailInfo(noteId)
>>>>>>> dev
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
<<<<<<< HEAD
                topicInfo.setValue(ContentService.retrofitService.getPageList(topicId.toString()))
=======
                topicInfo.setValue(pageRepository.loadPageList(topicId))
>>>>>>> dev
            } catch (e: Exception) {
            }
        }
    }

    fun openSliding() {
        _openSlidingPane.value = true
    }
}
