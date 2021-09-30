package com.thinlineit.ctrlf.page

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PageDao(
    val id: Int? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    val title: String? = null,
    val content: String? = null,
    @SerializedName("is_approved")
    val isApproved: Boolean? = null,
    val topic: Int? = null,
    val owners: List<Int>? = null
) : Parcelable
