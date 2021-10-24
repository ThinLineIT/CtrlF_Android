package com.thinlineit.ctrlf.page.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thinlineit.ctrlf.entity.Page

<<<<<<< HEAD:app/src/main/java/com/thinlineit/ctrlf/page/PageEditorViewModelFactory.kt
class PageEditorViewModelFactory(
    private val pageInfo: PageDao,
    private val topicTitle: String,
    private val topicId: Int
) : ViewModelProvider.Factory {
=======
class PageEditorViewModelFactory(private val pageInfo: Page) : ViewModelProvider.Factory {
>>>>>>> dev:app/src/main/java/com/thinlineit/ctrlf/page/editor/PageEditorViewModelFactory.kt
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PageEditorViewModel::class.java)) {
            return PageEditorViewModel(pageInfo, topicTitle, topicId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
