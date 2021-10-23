package com.thinlineit.ctrlf.issue

import com.google.gson.annotations.SerializedName

data class IssueListDao(
    @SerializedName("next_cursor")
    val nextCursor: Int,
    @SerializedName("notes")
    var issues: List<IssueDao>? = null
)
