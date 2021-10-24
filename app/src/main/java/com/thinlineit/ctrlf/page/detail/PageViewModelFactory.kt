package com.thinlineit.ctrlf.page.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PageViewModelFactory(private val noteId: Int) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PageViewModel::class.java)) {
            return PageViewModel(noteId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
