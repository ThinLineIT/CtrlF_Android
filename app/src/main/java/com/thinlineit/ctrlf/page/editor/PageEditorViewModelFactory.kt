package com.thinlineit.ctrlf.page.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thinlineit.ctrlf.entity.Page

class PageEditorViewModelFactory(
    private val pageInfo: Page,
    private val topicTitle: String,
    private val topicId: Int
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PageEditorViewModel::class.java)) {
            return PageEditorViewModel(pageInfo, topicTitle, topicId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
