package com.thinlineit.ctrlf.repository.dao

import com.thinlineit.ctrlf.data.request.PageCreateRequestBody
import com.thinlineit.ctrlf.entity.Note
import com.thinlineit.ctrlf.entity.Page
import com.thinlineit.ctrlf.entity.Topic
import com.thinlineit.ctrlf.repository.network.ContentService

class PageRepository {

    suspend fun loadPage(pageId: Int): Page {
        return ContentService.retrofitService.getPage(pageId.toString())
    }

    suspend fun loadNoteInfo(noteId: String): List<Topic> {
        return ContentService.retrofitService.getNote(noteId)
    }

    suspend fun loadNoteDetailInfo(noteId: String): Note {
        return ContentService.retrofitService.getNoteDetail(Integer.parseInt(noteId))
    }

    suspend fun loadPageList(topicId: Int): List<Page> {
        return ContentService.retrofitService.getPageList(topicId.toString())
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
