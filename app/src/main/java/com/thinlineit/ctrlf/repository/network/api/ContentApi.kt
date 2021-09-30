package com.thinlineit.ctrlf.repository.network.api

import com.thinlineit.ctrlf.notes.NoteDao
import com.thinlineit.ctrlf.notes.NoteListDao
import com.thinlineit.ctrlf.notes.TopicDao
import com.thinlineit.ctrlf.page.PageDao
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ContentApi {

    // 해당 노트 detail
    @GET("notes/{note_id}")
    suspend fun getNoteDetail(
        @Path("note_id") noteId: Int
    ): NoteDao

    // 모든 노트들을 조회, (search) 쿼리 값에 따라 결과 값을 걸러냄
    @GET("notes")
    suspend fun listNote(
        @Query("cursor") cursor: Int
    ): NoteListDao

    // note_id에 해당하는 topic들의 list를 리턴
    @GET("notes/{note_id}/topics")
    suspend fun getNote(
        @Path("note_id") noteId: String
    ): List<TopicDao>

    // 해당하는 페이지에 대한 정보를 리턴
    @GET("pages/{page_id}")
    suspend fun getPage(
        @Path("page_id") pageId: String
    ): PageDao

    @GET("topics/{topic_id}")
    suspend fun getTopic(
        @Path("topic_id") topicID: Int
    ): TopicDao

    // topic_id에 해당하는 page list를 리턴
    @GET("topics/{topic_id}/pages")
    suspend fun getPageList(
        @Path("topic_id") topic_id: String,
    ): List<PageDao>
}
