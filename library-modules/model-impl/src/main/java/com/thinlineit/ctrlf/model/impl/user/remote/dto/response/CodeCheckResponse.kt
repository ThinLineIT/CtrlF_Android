package com.thinlineit.ctrlf.model.impl.user.remote.dto.response

import com.google.gson.annotations.SerializedName

data class CodeCheckResponse(
    @SerializedName("signing_token")
    val signingToken: String
)
