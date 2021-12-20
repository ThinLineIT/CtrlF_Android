package com.thinlineit.ctrlf.page.editor

class PageEditorRepository {
    // TODO: using update pageAPI and return boolean type
    suspend fun complete(topicId: Int, title: String, content: String, summary: String): Boolean =
        try {
            true
        } catch (e: Exception) {
            false
        }
}
