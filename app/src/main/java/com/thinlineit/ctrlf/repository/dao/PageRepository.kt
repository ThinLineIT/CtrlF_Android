package com.thinlineit.ctrlf.repository.dao

import com.thinlineit.ctrlf.data.request.PageCreateRequestBody
import com.thinlineit.ctrlf.entity.Note
import com.thinlineit.ctrlf.entity.Page
import com.thinlineit.ctrlf.entity.Topic
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

    suspend fun createPage(topicId: Int, title: String, contents: String, summary: String): String {
        return try {
            ContentService.retrofitService.createPage(
                "Bearer " + com.thinlineit.ctrlf.util.Application.preferenceUtil.getString(
                    TOKEN,
                    ""
                ),
                PageCreateRequestBody(
                    topicId,
                    title,
                    contents,
                    summary
                )
            ).message()
        } catch (e: Exception) {
            e.toString()
        }
    }

    companion object {
        private const val TOKEN = "token"
    }
}
