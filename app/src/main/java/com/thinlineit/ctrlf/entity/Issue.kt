package com.thinlineit.ctrlf.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Issue(
    val id: Int,
    val owner: String,
    val title: String,
    val content: String,
    val status: String
) : Serializable

data class IssueList(
    @SerializedName("next_cursor")
    val nextCursor: Int,
    @SerializedName("issues")
    var issues: List<Issue>? = null
)
