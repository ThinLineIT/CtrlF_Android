package com.thinlineit.ctrlf.model.impl.user.remote.dto.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val token: String,
    @SerializedName("user_id")
    val userId: Int
)
