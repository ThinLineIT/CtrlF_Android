package com.thinlineit.ctrlf.issue

import java.io.Serializable

data class IssueDao(
    val id: Int,
    val owner: String,
    val title: String,
    val content: String,
    val status: String
) : Serializable
