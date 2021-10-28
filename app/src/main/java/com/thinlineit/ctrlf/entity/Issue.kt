package com.thinlineit.ctrlf.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Issue(
    @SerializedName("note_id")
    val noteId: Int? = null,
    @SerializedName("topic_id")
    val topicId: Int? = null,
    @SerializedName("page_id")
    val pageId: Int? = null,
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
