package com.thinlineit.ctrlf.issue

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Issue(
    val id: Int,
    val title: String,
    @SerializedName("note_id")
    val noteId: Int? = null,
    @SerializedName("topic_id")
    val topicId: Int? = null,
    @SerializedName("registration_date")
    val registrationDate: String? = null,
    val content: String
) : Serializable
