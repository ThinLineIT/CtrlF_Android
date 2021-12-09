package com.thinlineit.ctrlf.repository.dao

import android.net.Uri
import com.thinlineit.ctrlf.entity.Note
import com.thinlineit.ctrlf.entity.NoteList
import com.thinlineit.ctrlf.entity.Page
import com.thinlineit.ctrlf.entity.Topic
import com.thinlineit.ctrlf.repository.dto.request.PageCreateRequest
import com.thinlineit.ctrlf.repository.network.ContentService
import com.thinlineit.ctrlf.util.Application
import java.io.File
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ContentRepository {

    suspend fun loadPage(pageId: Int): Page {
        return ContentService.retrofitService.getPage(pageId)
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

    companion object {
        private const val TOKEN = "token"
        private const val SERVER_ERROR = 500
    }
}
