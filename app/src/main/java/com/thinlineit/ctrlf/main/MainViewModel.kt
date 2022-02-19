package com.thinlineit.ctrlf.main

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.entity.Issue
import com.thinlineit.ctrlf.entity.Note
import com.thinlineit.ctrlf.entity.NoteList
import com.thinlineit.ctrlf.main.banner.IdeaCountBannerFragment
import com.thinlineit.ctrlf.repository.dao.ContentRepository
import com.thinlineit.ctrlf.repository.dao.IssueRepository
import com.thinlineit.ctrlf.util.base.BaseViewModel

class MainViewModel(
    private val issueRepository: IssueRepository = IssueRepository(),
    private val contentRepository: ContentRepository = ContentRepository()
) : BaseViewModel() {

    private val _noteList = MutableLiveData<NoteList>()
    val noteList: LiveData<NoteList>
        get() = _noteList

    private val _issueList = MutableLiveData<List<Issue>>(emptyList())
    val issueList: LiveData<List<Issue>>
        get() = _issueList

    private val _fragmentList = MutableLiveData<List<Fragment>>(emptyList())
    val fragmentList: LiveData<List<Fragment>>
        get() = _fragmentList

    val issueCount = MutableLiveData<Int>()

    val notes: LiveData<List<Note>> = Transformations.map(noteList) { it.notes }

    var cursor: Int = 0

    init {
        loadBannerList()
        loadNote()
        loadIssue()
    }

    private fun loadBannerList() {
        _fragmentList.value = mutableListOf(IdeaCountBannerFragment())
    }

    fun loadNote() {
        viewModelScope.loadingLaunch {
            try {
                _noteList.value = contentRepository.loadNoteList(cursor)
                /* TODO: Implement loading with cursor by scrolling
                    .also {
                        cursor = it.nextCursor
                    }
                */
            } catch (e: Exception) {
            }
        }
    }

    fun loadIssue() {
        viewModelScope.loadingLaunch {
            try {
                _issueList.value = issueRepository.loadIssueList()
            } catch (e: Exception) {
            }
        }
    }

    fun loadIssueCount() {
        viewModelScope.loadingLaunch {
            try {
                issueCount.value = issueRepository.getIssueCount()
            } catch (e: Exception) {
            }
        }
    }
}
