package com.thinlineit.ctrlf.repository.dto.response

import com.google.gson.annotations.SerializedName

data class IssuesCountResponse(
    @SerializedName("issues_count")
    val issuesCount: Int
)
