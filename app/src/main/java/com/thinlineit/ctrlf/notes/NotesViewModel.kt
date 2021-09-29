package com.thinlineit.ctrlf.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.repository.network.ContentService
import com.thinlineit.ctrlf.util.base.BaseViewModel

class NotesViewModel : BaseViewModel() {
    private val _noteList = MutableLiveData<NoteListDao>()
    val noteList: LiveData<NoteListDao>
        get() = _noteList
    val alertLiveData = MutableLiveData<String>()

    val notes = Transformations.map(noteList) { it.notes }

    init {
        loadNote()
    }

    private fun loadNote() {
        viewModelScope.loadingLaunch {
            try {
                _noteList.value = ContentService.retrofitService.listNote(0)
            } catch (e: Exception) {
                alertLiveData.postValue(e.toString())
            }
        }
    }
}
