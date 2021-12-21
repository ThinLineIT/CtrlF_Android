package com.thinlineit.ctrlf.repository.dto.request

import com.google.gson.annotations.SerializedName

class PageUpdateRequest(
    @SerializedName("new_title")
    val newTitle: String,
    @SerializedName("new_content")
    val newContent: String,
    val reason: String
)
