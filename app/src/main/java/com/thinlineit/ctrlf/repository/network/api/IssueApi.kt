package com.thinlineit.ctrlf.repository.network.api

import com.thinlineit.ctrlf.entity.Issue
import com.thinlineit.ctrlf.entity.IssueList
import com.thinlineit.ctrlf.repository.dto.request.IssueApproveRequest
import com.thinlineit.ctrlf.repository.dto.response.IssueCountResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface IssueApi {
    @GET("issues")
    suspend fun listIssue(
        @Query("cursor") cursor: Int
    ): IssueList

    @GET("issues/{issue_id}")
    suspend fun detailIssue(
        @Path("issue_id") issueId: String,
    ): Issue

    @POST("actions/issue-approve/")
    suspend fun approveIssue(
        @Header("Authorization") Authorization: String,
        @Body body: IssueApproveRequest
    ): Response<Void>

    @GET("issues/issue-count/")
    suspend fun issueCount(): IssueCountResponse
}
