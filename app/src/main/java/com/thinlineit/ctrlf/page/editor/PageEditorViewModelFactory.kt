package com.thinlineit.ctrlf.page.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thinlineit.ctrlf.entity.Page

class PageEditorViewModelFactory(private val pageInfo: Page) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PageEditorViewModel::class.java)) {
            return PageEditorViewModel(pageInfo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
