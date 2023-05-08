package com.thinlineit.ctrlf.repository.dto.request

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    val nickname: String,
    val password: String,
    @SerializedName("password_confirm")
    val passwordConfirm: String,
    @SerializedName("signing_token")
    val signingToken: String

)
