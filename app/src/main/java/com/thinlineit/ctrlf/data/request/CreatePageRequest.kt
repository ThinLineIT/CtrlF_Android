package com.thinlineit.ctrlf.data.request

import com.google.gson.annotations.SerializedName

data class CreatePageRequest(
    @SerializedName("topic_id")
    val topicId: Int,
    val title: String,
    val contents: String,
    val summary: String
)
