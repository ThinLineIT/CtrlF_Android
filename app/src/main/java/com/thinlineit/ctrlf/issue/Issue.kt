package com.thinlineit.ctrlf.issue

import java.io.Serializable

data class Issue(
    val id: Int,
    val owner: String,
    val title: String,
    val content: String,
    val status: String
) : Serializable
