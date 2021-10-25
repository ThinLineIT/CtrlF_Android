package com.thinlineit.ctrlf.repository.dto.request

data class TopicCreateRequest(
    val noteId: Int,
    val title: String,
    val content: String
)
