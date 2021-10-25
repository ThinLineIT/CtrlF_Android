package com.thinlineit.ctrlf.issue

import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface IssueApi {

    // 모든 이슈들을 조회, (search, note_id, topic_id, limit) 쿼리 값에 따라 결과 값을 걸러냄
    @GET("issues")
    suspend fun listIssue(
        @Query("search") search: String,
        @Query("note_id") noteId: String,
        @Query("topic_id") topicId: String,
        @Query("limit") limit: Int
    ): List<Issue>

    // Issue 생성
    @FormUrlEncoded
    @POST("issues")
    suspend fun addIssue(
        @Field("title") title: String,
        @Field("note_id") noteId: Int,
        @Field("topic_id") topicId: Int,
        @Field("content") content: String
    )

    // Issue 수정
    @FormUrlEncoded
    @PATCH("issues/{issue_id}")
    suspend fun updateIssue(
        @Path("issue_id") issueId: Int,
        @Field("title") title: String,
        @Field("note_id") noteId: Int,
        @Field("topic_id") topicId: Int,
        @Field("content") content: String
    )

    // Issue 삭제
    @DELETE("issues/{issue_id}")
    suspend fun deleteIssue(
        @Path("issue_id") issueId: Int
    )
}
