package com.thinlineit.ctrlf.repository.dto.request

import com.google.gson.annotations.SerializedName

data class TopicCreateRequest(
    @SerializedName("note_id")
    val noteId: Int,
    val title: String,
    val reason: String
)
