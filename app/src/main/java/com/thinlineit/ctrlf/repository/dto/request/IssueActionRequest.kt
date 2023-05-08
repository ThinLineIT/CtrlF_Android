package com.thinlineit.ctrlf.repository.dto.request

import com.google.gson.annotations.SerializedName

data class IssueActionRequest(
    @SerializedName("issue_id")
    val issueId: Int
)
