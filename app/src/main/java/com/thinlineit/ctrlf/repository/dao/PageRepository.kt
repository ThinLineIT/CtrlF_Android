package com.thinlineit.ctrlf.repository.dao

import com.thinlineit.ctrlf.entity.Note
import com.thinlineit.ctrlf.entity.Page
import com.thinlineit.ctrlf.entity.Topic
import com.thinlineit.ctrlf.repository.dto.request.PageCreateRequest
import com.thinlineit.ctrlf.repository.network.ContentService

class PageRepository {

    suspend fun loadPage(pageId: Int): Page {
        return ContentService.retrofitService.getPage(pageId)
    }

    suspend fun loadTopicList(noteId: Int): List<Topic> {
        return ContentService.retrofitService.getNote(noteId)
    }

    suspend fun loadNote(noteId: Int): Note {
        return ContentService.retrofitService.getNoteDetail(noteId)
    }

    suspend fun loadPageList(topicId: Int): List<Page> {
        return ContentService.retrofitService.getPageList(topicId)
    }

    suspend fun createPage(topicId: Int, title: String, content: String, summary: String): Int {
        return try {
            ContentService.retrofitService.createPage(
                "Bearer " + com.thinlineit.ctrlf.util.Application.preferenceUtil.getString(
                    TOKEN,
                    ""
                ),
                PageCreateRequest(
                    topicId,
                    title,
                    content,
                    summary
                )
            ).code()
        } catch (e: Exception) {
            SERVER_ERROR
        }
    }

    companion object {
        private const val TOKEN = "token"
        private const val SERVER_ERROR = 500
    }
}
