package com.thinlineit.ctrlf.data.response

import com.google.gson.annotations.SerializedName

data class AuthEmailResponse(
    @SerializedName("signing_token")
    val signingToken: String
)
