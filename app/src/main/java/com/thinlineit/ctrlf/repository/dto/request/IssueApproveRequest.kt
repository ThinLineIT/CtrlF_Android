package com.thinlineit.ctrlf.repository.dto.request

import com.google.gson.annotations.SerializedName

data class IssueApproveRequest(
    @SerializedName("issue_id")
    val issueId: Int
)
