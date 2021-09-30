package com.thinlineit.ctrlf.main

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.issue.IssueDao
import com.thinlineit.ctrlf.main.viewpager.IdeaCountBannerFragment
import com.thinlineit.ctrlf.notes.NoteDao
import com.thinlineit.ctrlf.notes.NoteListDao
import com.thinlineit.ctrlf.repository.network.ContentService
import com.thinlineit.ctrlf.util.base.BaseViewModel

class MainViewModel : BaseViewModel() {
    private val _noteList = MutableLiveData<NoteListDao>()
    val noteList: LiveData<NoteListDao>
        get() = _noteList

    private val _issueList = MutableLiveData<List<IssueDao>>(emptyList())
    val issueList: LiveData<List<IssueDao>>
        get() = _issueList

    private val _fragmentList = MutableLiveData<List<Fragment>>(emptyList())
    val fragmentList: LiveData<List<Fragment>>
        get() = _fragmentList

    val notes: LiveData<List<NoteDao>> = Transformations.map(noteList) { it.notes }

    var cursor: Int = 0

    init {
        loadBannerList()
        loadNote()
    }

    private fun loadBannerList() {
        _fragmentList.value = mutableListOf(IdeaCountBannerFragment())
    }

    private fun loadNote() {
        viewModelScope.loadingLaunch {
            try {
                _noteList.value = ContentService.retrofitService.listNote(cursor)
                /* TODO: Implement loading with cursor by scrolling
                    .also {
                        cursor = it.nextCursor
                    }
                */
            } catch (e: Exception) {
            }
        }
    }

    private fun loadIssue() {
        // TODO: Load the list of issue using "getIssue" api
        _issueList.value = createIssue()
    }

    private fun createIssue(): List<IssueDao> {
        val contentStr =
            "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz" +
                "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
        return (1..9).map { i ->
            if (i % 2 != 0) IssueDao(i, "title$i", 1, 1, "2021-07-12", contentStr)
            else IssueDao(i, "title$i", 1, 1, "2021-07-12", contentStr + contentStr)
        }
    }
}
