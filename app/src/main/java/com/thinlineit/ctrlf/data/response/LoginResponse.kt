package com.thinlineit.ctrlf.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val token: String,
    @SerializedName("user_id")
    val userId: Int
)
