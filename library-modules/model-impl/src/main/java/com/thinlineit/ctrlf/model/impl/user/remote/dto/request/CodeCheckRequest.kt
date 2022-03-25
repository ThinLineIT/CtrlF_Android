package com.thinlineit.ctrlf.model.impl.user.remote.dto.request

import com.google.gson.annotations.SerializedName

data class CodeCheckRequest(
    val code: String,
    @SerializedName("signing_token")
    val signingToken: String
)
