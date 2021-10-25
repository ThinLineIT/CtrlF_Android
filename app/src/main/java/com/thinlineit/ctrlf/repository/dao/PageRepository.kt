package com.thinlineit.ctrlf.repository.dao

<<<<<<< HEAD:app/src/main/java/com/thinlineit/ctrlf/repository/PageRepository.kt
import com.thinlineit.ctrlf.data.request.CreatePageRequest
import com.thinlineit.ctrlf.notes.NoteDao
import com.thinlineit.ctrlf.notes.TopicDao
import com.thinlineit.ctrlf.page.PageDao
=======
import com.thinlineit.ctrlf.entity.Note
import com.thinlineit.ctrlf.entity.Page
import com.thinlineit.ctrlf.entity.Topic
>>>>>>> dev:app/src/main/java/com/thinlineit/ctrlf/repository/dao/PageRepository.kt
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

    suspend fun createPage(topicId: Int, title: String, contents: String, summary: String): Int {
        return try {
            ContentService.retrofitService.createPage(
                "Bearer" + com.thinlineit.ctrlf.util.Application.preferenceUtil.getString(
                    TOKEN,
                    ""
                ),
                CreatePageRequest(
                    topicId,
                    title,
                    contents,
                    summary
                )
            ).code()
        } catch (e: Exception) {
            500
        }
    }

    companion object {
        private const val TOKEN = "token"
    }
}
