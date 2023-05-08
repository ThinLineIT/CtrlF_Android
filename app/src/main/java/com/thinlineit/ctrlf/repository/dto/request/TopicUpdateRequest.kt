package com.thinlineit.ctrlf.repository.dto.request

import com.google.gson.annotations.SerializedName

data class TopicUpdateRequest(
    @SerializedName("new_title")
    val newTitle: String,
    val reason: String
)
