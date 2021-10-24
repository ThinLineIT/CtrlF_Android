package com.thinlineit.ctrlf.page.editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thinlineit.ctrlf.entity.Page

class PageEditorViewModel(private val pageInfo: Page) : ViewModel() {
    private val pageEditorRepository = PageEditorRepository()
    val content = MutableLiveData<String>(pageInfo.content)

    fun complete() {
        // TODO: if it's true, inform success
        pageEditorRepository.complete(pageInfo)
    }
}
