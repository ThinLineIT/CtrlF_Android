package com.thinlineit.ctrlf.repository.network.api

import com.thinlineit.ctrlf.entity.Note
import com.thinlineit.ctrlf.entity.NoteList
import com.thinlineit.ctrlf.entity.Page
import com.thinlineit.ctrlf.entity.Topic
import com.thinlineit.ctrlf.repository.dto.request.NoteCreateRequest
import com.thinlineit.ctrlf.repository.dto.request.NoteDeleteRequest
import com.thinlineit.ctrlf.repository.dto.request.NoteUpdateRequest
import com.thinlineit.ctrlf.repository.dto.request.PageCreateRequest
import com.thinlineit.ctrlf.repository.dto.request.PageDeleteRequest
import com.thinlineit.ctrlf.repository.dto.request.PageUpdateRequest
import com.thinlineit.ctrlf.repository.dto.request.TopicCreateRequest
import com.thinlineit.ctrlf.repository.dto.request.TopicDeleteRequest
import com.thinlineit.ctrlf.repository.dto.request.TopicUpdateRequest
import com.thinlineit.ctrlf.repository.dto.response.ImageUploadResponse
import com.thinlineit.ctrlf.repository.dto.response.NoteDeleteResponse
import com.thinlineit.ctrlf.repository.dto.response.NoteUpdateResponse
import com.thinlineit.ctrlf.repository.dto.response.PageDeleteResponse
import com.thinlineit.ctrlf.repository.dto.response.TopicDeleteResponse
import com.thinlineit.ctrlf.repository.dto.response.TopicUpdateResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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
        @Path("note_id") noteId: Int
    ): List<Topic>

    // 해당하는 페이지에 대한 정보를 리턴
    @GET("pages/{page_id}")
    suspend fun getPage(
        @Path("page_id") pageId: Int,
        @Query("version_no") versionNo: Int
    ): Page

    @GET("topics/{topic_id}")
    suspend fun getTopic(
        @Path("topic_id") topicID: Int
    ): Topic

    // topic_id에 해당하는 page list를 리턴
    @GET("topics/{topic_id}/pages")
    suspend fun getPageList(
        @Path("topic_id") topic_id: Int
    ): List<Page>

    @POST("pages/")
    suspend fun createPage(
        @Header("Authorization") Authorization: String,
        @Body body: PageCreateRequest
    ): Response<Void>

    @Multipart
    @POST("actions/images/")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): ImageUploadResponse

    // note_id에 해당하는 note에 새로운 토픽 생성
    @POST("topics/")
    suspend fun createTopic(
        @Header("Authorization") Authorization: String,
        @Body request: TopicCreateRequest
    ): Response<Void>

    // 새로운 노트 생성
    @POST("notes/")
    suspend fun createNote(
        @Header("Authorization") Authorization: String,
        @Body request: NoteCreateRequest
    ): Response<Void>

    // 토픽 수정
    @PUT("topics/{topic_id}/")
    suspend fun updateTopic(
        @Header("Authorization") Authorization: String,
        @Path("topic_id") topicId: Int,
        @Body request: TopicUpdateRequest
    ): TopicUpdateResponse

    // 노트 수정
    @PUT("notes/{note_id}/")
    suspend fun updateNote(
        @Header("Authorization") Authorization: String,
        @Path("note_id") noteId: Int,
        @Body request: NoteUpdateRequest
    ): NoteUpdateResponse

    @PUT("pages/{page_id}/")
    suspend fun updatePage(
        @Header("Authorization") Authorization: String,
        @Path("page_id") pageId: Int,
        @Body request: PageUpdateRequest
    )

    @HTTP(method = "DELETE", path = "pages/{page_id}/", hasBody = true)
    suspend fun deletePage(
        @Header("Authorization") Authorization: String,
        @Path("page_id") pageId: Int,
        @Body request: PageDeleteRequest
    ): PageDeleteResponse

    @HTTP(method = "DELETE", path = "notes/{note_id}/", hasBody = true)
    suspend fun deleteNote(
        @Header("Authorization") Authorization: String,
        @Path("note_id") noteId: Int,
        @Body request: NoteDeleteRequest
    ): NoteDeleteResponse

    @HTTP(method = "DELETE", path = "topics/{topic_id}/", hasBody = true)
    suspend fun deleteTopic(
        @Header("Authorization") Authorization: String,
        @Path("topic_id") topicId: Int,
        @Body request: TopicDeleteRequest
    ): TopicDeleteResponse
}
