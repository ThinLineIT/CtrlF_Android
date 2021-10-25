package com.thinlineit.ctrlf.repository.network.api

import com.thinlineit.ctrlf.data.request.CreatePageRequest
import com.thinlineit.ctrlf.entity.Note
import com.thinlineit.ctrlf.entity.NoteList
import com.thinlineit.ctrlf.entity.Page
import com.thinlineit.ctrlf.entity.Topic
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ContentApi {

    // 해당 노트 detail
    @GET("notes/{note_id}")
    suspend fun getNoteDetail(
        @Path("note_id") noteId: Int
    ): Note

    // 모든 노트들을 조회, (search) 쿼리 값에 따라 결과 값을 걸러냄
    @GET("notes")
    suspend fun listNote(
        @Query("cursor") cursor: Int
    ): NoteList

    // note_id에 해당하는 topic들의 list를 리턴
    @GET("notes/{note_id}/topics")
    suspend fun getNote(
        @Path("note_id") noteId: String
    ): List<Topic>

    // 해당하는 페이지에 대한 정보를 리턴
    @GET("pages/{page_id}")
    suspend fun getPage(
        @Path("page_id") pageId: String
    ): Page

    @GET("topics/{topic_id}")
    suspend fun getTopic(
        @Path("topic_id") topicID: Int
    ): Topic

    // topic_id에 해당하는 page list를 리턴
    @GET("topics/{topic_id}/pages")
    suspend fun getPageList(
        @Path("topic_id") topic_id: String,
    ): List<Page>

    @POST("pages")
    suspend fun createPage(
        @Header("Auth") Auth: String,
        @Body body: CreatePageRequest
    ): Response<Body>
}
