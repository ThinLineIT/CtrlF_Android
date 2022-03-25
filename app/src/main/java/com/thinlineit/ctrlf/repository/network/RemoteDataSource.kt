package com.thinlineit.ctrlf.repository.network

import com.thinlineit.ctrlf.repository.network.api.ContentApi
import com.thinlineit.ctrlf.repository.network.api.IssueApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://testdeploy-dev.ap-northeast-2.elasticbeanstalk.com/api/"
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object ContentService {
    val retrofitService: ContentApi by lazy {
        retrofit.create(ContentApi::class.java)
    }
}

object IssueService {
    val retrofitService: IssueApi by lazy {
        retrofit.create(IssueApi::class.java)
    }
}
