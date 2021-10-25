package com.thinlineit.ctrlf.repository.dto.response

import com.google.gson.annotations.SerializedName

data class CodeCheckResponse(
    @SerializedName("signing_token")
    val signingToken: String
)
