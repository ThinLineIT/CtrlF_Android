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
    @SerializedName("version_no")
    val versionNo: Int? = null,
    val id: Int,
    val owner: String? = null,
    val title: String,
    val reason: String,
    val status: String? = null,
    @SerializedName("related_model_type")
    val relatedModelType: String,
    val action: String,
    @SerializedName("legacy_title")
    val legacyTitle: String? = null
) : Serializable

data class IssueList(
    @SerializedName("next_cursor")
    val nextCursor: Int,
    @SerializedName("issues")
    var issues: List<Issue>? = null
)
