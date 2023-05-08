package com.thinlineit.ctrlf.issue

data class IssueMaterial(
    val contentId: Int?,
    val title: String?,
    val reason: String?,
    val contentText: String? = null
)
