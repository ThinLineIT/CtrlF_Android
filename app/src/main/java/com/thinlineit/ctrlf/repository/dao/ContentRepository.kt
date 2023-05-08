package com.thinlineit.ctrlf.repository.dao

import android.net.Uri
import com.thinlineit.ctrlf.entity.Note
import com.thinlineit.ctrlf.entity.NoteList
import com.thinlineit.ctrlf.entity.Page
import com.thinlineit.ctrlf.entity.Topic
import com.thinlineit.ctrlf.repository.dto.request.NoteCreateRequest
import com.thinlineit.ctrlf.repository.dto.request.NoteDeleteRequest
import com.thinlineit.ctrlf.repository.dto.request.NoteUpdateRequest
import com.thinlineit.ctrlf.repository.dto.request.PageCreateRequest
import com.thinlineit.ctrlf.repository.dto.request.PageDeleteRequest
import com.thinlineit.ctrlf.repository.dto.request.PageUpdateRequest
import com.thinlineit.ctrlf.repository.dto.request.TopicCreateRequest
import com.thinlineit.ctrlf.repository.dto.request.TopicDeleteRequest
import com.thinlineit.ctrlf.repository.dto.request.TopicUpdateRequest
import com.thinlineit.ctrlf.repository.network.ContentService
import com.thinlineit.ctrlf.util.Application
import java.io.File
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ContentRepository {

    suspend fun loadPage(pageId: Int, versionNo: Int): Page {
        return ContentService.retrofitService.getPage(pageId, versionNo)
    }

    suspend fun loadTopic(topicId: Int): Topic {
        return ContentService.retrofitService.getTopic(topicId)
    }

    suspend fun loadNoteList(cursor: Int): NoteList {
        return ContentService.retrofitService.listNote(cursor)
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

    suspend fun createNote(title: String, reason: String): Boolean {
        return try {
            ContentService.retrofitService.createNote(
                "Bearer " + Application.preferenceUtil.getString(
                    TOKEN,
                    ""
                ),
                NoteCreateRequest(
                    title,
                    reason
                )
            )
            true
        } catch (e: Exception) {
            e.toString()
            false
        }
    }

    suspend fun createTopic(noteId: Int, title: String, reason: String): String {
        return try {
            ContentService.retrofitService.createTopic(
                "Bearer " + com.thinlineit.ctrlf.util.Application.preferenceUtil.getString(
                    TOKEN,
                    ""
                ),
                TopicCreateRequest(
                    noteId,
                    title,
                    reason
                )
            ).message()
        } catch (e: java.lang.Exception) {
            e.toString()
        }
    }

    suspend fun createPage(topicId: Int, title: String, content: String, summary: String): Int {
        return try {
            ContentService.retrofitService.createPage(
                "Bearer " + Application.preferenceUtil.getString(
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

    suspend fun uploadImage(uri: Uri, fileName: String): String {
        return try {
            val file = File(uri.path)
            val requestImage: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val fileBody: MultipartBody.Part =
                MultipartBody.Part.createFormData("image", fileName, requestImage)
            ContentService.retrofitService.uploadImage(fileBody).imageUrl
        } catch (e: Exception) {
            e.toString()
        }
    }

    suspend fun updateTopic(topicId: Int, newTopicTitle: String, reason: String): Boolean {
        return try {
            ContentService.retrofitService.updateTopic(
                "Bearer " + Application.preferenceUtil.getString(
                    TOKEN,
                    ""
                ),
                topicId,
                TopicUpdateRequest(newTopicTitle, reason)
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateNote(noteId: Int, newNoteTitle: String, reason: String): Boolean {
        return try {
            ContentService.retrofitService.updateNote(
                "Bearer " + Application.preferenceUtil.getString(
                    TOKEN,
                    ""
                ),
                noteId,
                NoteUpdateRequest(newNoteTitle, reason)
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updatePage(
        pageId: Int,
        newPageTitle: String,
        newContent: String,
        reason: String
    ): Boolean {
        return try {
            ContentService.retrofitService.updatePage(
                "Bearer " + Application.preferenceUtil.getString(
                    TOKEN,
                    ""
                ),
                pageId,
                PageUpdateRequest(newPageTitle, newContent, reason)
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deletePage(
        pageId: Int,
        reason: String
    ): Boolean {
        return try {
            ContentService.retrofitService.deletePage(
                "Bearer " + Application.preferenceUtil.getString(
                    TOKEN,
                    ""
                ),
                pageId,
                PageDeleteRequest(reason)
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteTopic(
        topicId: Int,
        reason: String
    ): Boolean {
        return try {
            ContentService.retrofitService.deleteTopic(
                "Bearer " + Application.preferenceUtil.getString(
                    TOKEN,
                    ""
                ),
                topicId,
                TopicDeleteRequest(reason)
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteNote(
        noteId: Int,
        reason: String
    ): Boolean {
        return try {
            ContentService.retrofitService.deleteNote(
                "Bearer " + Application.preferenceUtil.getString(
                    TOKEN,
                    ""
                ),
                noteId,
                NoteDeleteRequest(reason)
            )
            true
        } catch (e: Exception) {
            println(e)
            false
        }
    }

    companion object {
        private const val TOKEN = "token"
        private const val SERVER_ERROR = 500
    }
}
