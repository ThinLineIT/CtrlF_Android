package com.thinlineit.ctrlf.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

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
    val owners: List<Int>,
    val topicList: List<Topic>? = null
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
    val owners: List<Int>,
    val pageList: List<Page>? = null
)

@Parcelize
data class Page(
    val id: Int? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    val title: String? = null,
    val content: String? = null,
    @SerializedName("is_approved")
    val isApproved: Boolean? = null,
    val topic: Int? = null,
    val owners: List<Int>? = null
) : Parcelable
