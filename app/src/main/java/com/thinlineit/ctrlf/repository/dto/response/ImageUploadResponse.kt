package com.thinlineit.ctrlf.repository.dto.response

import com.google.gson.annotations.SerializedName

data class ImageUploadResponse(
    @SerializedName("image_url")
    val imageUrl: String
)
