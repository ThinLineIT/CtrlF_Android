package com.thinlineit.ctrlf.model.impl.user.remote.dto.request

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("new_password")
    val newPassword: String,
    @SerializedName("new_password_confirm")
    val newPasswordConfirm: String,
    @SerializedName("signing_token")
    val signingToken: String
)
