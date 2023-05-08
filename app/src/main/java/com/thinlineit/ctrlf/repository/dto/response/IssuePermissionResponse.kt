package com.thinlineit.ctrlf.repository.dto.response

import com.google.gson.annotations.SerializedName

data class IssuePermissionResponse(
    @SerializedName("has_permission")
    val hasPermission: Boolean
)
