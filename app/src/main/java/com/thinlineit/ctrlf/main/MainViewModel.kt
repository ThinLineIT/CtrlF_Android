package com.thinlineit.ctrlf.main

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.issue.Issue
import com.thinlineit.ctrlf.main.banner.IdeaCountBannerFragment
import com.thinlineit.ctrlf.entity.Note
import com.thinlineit.ctrlf.entity.NoteList
import com.thinlineit.ctrlf.repository.network.ContentService
import com.thinlineit.ctrlf.util.base.BaseViewModel

class MainViewModel : BaseViewModel() {
    private val _noteList = MutableLiveData<NoteList>()
    val noteList: LiveData<NoteList>
        get() = _noteList

    private val _issueList = MutableLiveData<List<Issue>>(emptyList())
    val issueList: LiveData<List<Issue>>
        get() = _issueList

    private val _fragmentList = MutableLiveData<List<Fragment>>(emptyList())
    val fragmentList: LiveData<List<Fragment>>
        get() = _fragmentList

    val notes: LiveData<List<Note>> = Transformations.map(noteList) { it.notes }

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

    private fun createIssue(): List<Issue> {
        val contentStr =
            "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz" +
                "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
        return (1..9).map { i ->
            if (i % 2 != 0) Issue(i, "title$i", 1, 1, "2021-07-12", contentStr)
            else Issue(i, "title$i", 1, 1, "2021-07-12", contentStr + contentStr)
        }
    }
}
