package com.thinlineit.ctrlf.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.entity.NoteList
import com.thinlineit.ctrlf.repository.dao.ContentRepository
import com.thinlineit.ctrlf.util.base.BaseViewModel
import kotlinx.coroutines.withContext

class NotesViewModel(
    private val contentRepository: ContentRepository = ContentRepository()
) : BaseViewModel() {
    private val _noteList = MutableLiveData<NoteList>()
    val noteList: LiveData<NoteList>
        get() = _noteList
    val alertLiveData = MutableLiveData<String>()

    val notes = Transformations.map(noteList) { it.notes }

    var cursor: Int = 0

    init {
        loadNote()
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

    suspend fun createNote(noteTitleEdit: String, reasonEdit: String): Boolean {
        return withContext(viewModelScope.coroutineContext) {
            if (noteTitleEdit == "" || reasonEdit == "") return@withContext false
            if (!contentRepository.createNote(noteTitleEdit, reasonEdit)) return@withContext false
            loadNote()
            return@withContext true
        }
    }
}
