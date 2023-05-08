package com.thinlineit.ctrlf.repository.dto.request

import com.google.gson.annotations.SerializedName

data class IssueUpdateActionRequest(
    @SerializedName("issue_id")
    val issueId: Int,
    @SerializedName("new_title")
    val newTitle: String? = null,
    @SerializedName("new_content")
    val newContent: String? = null,
    val reason: String
)
