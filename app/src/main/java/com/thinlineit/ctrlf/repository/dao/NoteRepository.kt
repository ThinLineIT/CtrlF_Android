package com.thinlineit.ctrlf.repository.dao

import com.thinlineit.ctrlf.repository.dto.request.NoteCreateRequest
import com.thinlineit.ctrlf.repository.network.ContentService

class NoteRepository {
    suspend fun createNote(title: String, reason: String): String {
        return try {
            ContentService.retrofitService.createNote(
                "Bearer " + com.thinlineit.ctrlf.util.Application.preferenceUtil.getString(
                    TOKEN,
                    ""
                ),
                NoteCreateRequest(
                    title,
                    reason
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
