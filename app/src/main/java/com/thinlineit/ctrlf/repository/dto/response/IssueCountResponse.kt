package com.thinlineit.ctrlf.repository.dto.response

import com.google.gson.annotations.SerializedName

data class IssueCountResponse(
    @SerializedName("issues_count")
    val issuesCount: Int
)
