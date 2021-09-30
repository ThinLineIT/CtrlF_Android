package com.thinlineit.ctrlf.notes

import com.google.gson.annotations.SerializedName

data class NoteDao(
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

data class TopicDao(
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
