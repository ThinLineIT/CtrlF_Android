package com.thinlineit.ctrlf.repository.network.api

import com.thinlineit.ctrlf.entity.Issue
import com.thinlineit.ctrlf.entity.IssueList
import com.thinlineit.ctrlf.repository.dto.request.IssueActionRequest
import com.thinlineit.ctrlf.repository.dto.request.IssueUpdateActionRequest
import com.thinlineit.ctrlf.repository.dto.response.IssueCountResponse
import com.thinlineit.ctrlf.repository.dto.response.IssuePermissionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
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
        @Body body: IssueActionRequest
    ): Response<Unit>

    @POST("actions/issue-close/")
    suspend fun closeIssue(
        @Header("Authorization") Authorization: String,
        @Body body: IssueActionRequest
    ): Response<Void>

    @POST("actions/issue-reject/")
    suspend fun rejectIssue(
        @Header("Authorization") Authorization: String,
        @Body body: IssueActionRequest
    ): Response<Void>

    @POST("actions/issue-update/")
    suspend fun updateIssue(
        @Header("Authorization") Authorization: String,
        @Body body: IssueUpdateActionRequest
    ): Response<Void>

    @POST("actions/issue-update-permission-check/")
    suspend fun checkPermissionIssue(
        @Header("Authorization") Authorization: String,
        @Body body: IssueActionRequest
    ): IssuePermissionResponse

    @HTTP(method = "DELETE", hasBody = true, path = "actions/issue-delete/")
    suspend fun deleteIssue(
        @Header("Authorization") Authorization: String,
        @Body body: IssueActionRequest
    ): Response<Void>

    @GET("issues/count/")
    suspend fun issueCount(): IssueCountResponse
}
