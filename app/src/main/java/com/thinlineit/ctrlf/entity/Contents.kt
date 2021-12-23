package com.thinlineit.ctrlf.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

const val UNSET_ID = -1

data class NoteList(
    @SerializedName("next_cursor")
    val nextCursor: Int,
    @SerializedName("notes")
    var notes: List<Note>? = null
)

data class Note(
    val id: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updateAt: String,
    val title: String,
    @SerializedName("is_approved")
    val isApproved: Boolean,
    val owners: List<Int>
)

data class Topic(
    val id: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updateAt: String,
    val title: String,
    @SerializedName("is_approved")
    val isApproved: Boolean,
    val note: Int,
    val owners: List<Int>
)

@Parcelize
data class Page(
    val id: Int = -1,
    @SerializedName("topic")
    val topicId: Int? = null,
    val owners: List<Int>? = null,
    @SerializedName("issue_id")
    val issueId: Int? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    val title: String? = null,
    val content: String? = null,
    @SerializedName("is_approved")
    val isApproved: Boolean? = null,
    @SerializedName("version_no")
    val versionNo: Int = -1,
    @SerializedName("version_type")
    val versionType: String? = null,
) : Parcelable
