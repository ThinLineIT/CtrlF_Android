package com.thinlineit.ctrlf.repository.network.api

import com.thinlineit.ctrlf.entity.IssueList
import retrofit2.http.GET
import retrofit2.http.Query

interface IssueApi {
    @GET("issues")
    suspend fun listIssue(
        @Query("cursor") cursor: Int,
    ): IssueList
}
