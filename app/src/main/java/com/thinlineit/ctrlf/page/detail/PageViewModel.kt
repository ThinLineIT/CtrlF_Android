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

class PageViewModel(noteId: Int) : BaseViewModel() {

    private val pageRepository: PageRepository by lazy {
        PageRepository()
    }
    private val noteIdString = MutableLiveData(noteId.toString())
<<<<<<< HEAD:app/src/main/java/com/thinlineit/ctrlf/page/PageViewModel.kt
    val pageInfo = MutableLiveData<PageDao>()
    private val noteDetailInfo = MutableLiveData<NoteDao>()
=======
    private val pageInfo = MutableLiveData<Page>()
    private val noteDetailInfo = MutableLiveData<Note>()
>>>>>>> dev:app/src/main/java/com/thinlineit/ctrlf/page/detail/PageViewModel.kt

    private val _isRightPaneOpen = MutableLiveData<Boolean>(false)
    val isRightPaneOpen: LiveData<Boolean>
        get() = _isRightPaneOpen

<<<<<<< HEAD:app/src/main/java/com/thinlineit/ctrlf/page/PageViewModel.kt
    val noteInfo = MutableLiveData<List<TopicDao>>(listOf())
    val topicInfo = MutableLiveData<List<PageDao>>()
    val topicIdInfo = MutableLiveData<Int>()
=======
    private val _isFabOpen = MutableLiveData<Boolean>(false)
    val isFabOpen: LiveData<Boolean>
        get() = _isFabOpen

    val noteInfo = MutableLiveData<List<Topic>>(listOf())
    val topicInfo = MutableLiveData<List<Page>>()
>>>>>>> dev:app/src/main/java/com/thinlineit/ctrlf/page/detail/PageViewModel.kt
    val content = Transformations.map(pageInfo) { it.content }
    val pageTitle = Transformations.map(pageInfo) { it.title }
    val topicTitleTop = MutableLiveData<String>()
    val noteDetailTitle = Transformations.map(noteDetailInfo) { it.title }

    lateinit var topicDetailTitle: String

    init {
        loadNoteInfo()
        loadNoteDetailInfo()
        closeRightPane()
    }

    fun openPage(pageId: Int) {
        loadPage(pageId)
    }

    private fun loadPage(pageId: Int) {
        viewModelScope.loadingLaunch {
            try {
                pageInfo.value = pageRepository.loadPage(pageId)
                openRightPane()
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

    fun selectTopic(topicId: Int, topicTitle: String) {
        loadPageList(topicId)
        topicDetailTitle = topicTitle
        topicTitleTop.value = topicTitle
        topicIdInfo.value = topicId
    }

    private fun loadPageList(topicId: Int) {
        viewModelScope.loadingLaunch {
            try {
                topicInfo.setValue(pageRepository.loadPageList(topicId))
            } catch (e: Exception) {
            }
        }
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
}
