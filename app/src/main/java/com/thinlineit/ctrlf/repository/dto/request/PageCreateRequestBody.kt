package com.thinlineit.ctrlf.repository.dto.request

import com.google.gson.annotations.SerializedName

data class PageCreateRequestBody(
    @SerializedName("topic_id")
    val topicId: Int,
    val title: String,
    val content: String,
    val summary: String
)
