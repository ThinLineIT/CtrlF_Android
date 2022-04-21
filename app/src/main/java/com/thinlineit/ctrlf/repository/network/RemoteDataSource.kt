package com.thinlineit.ctrlf.repository.network

import com.google.gson.GsonBuilder
import com.thinlineit.ctrlf.repository.network.api.ContentApi
import com.thinlineit.ctrlf.repository.network.api.IssueApi
import com.thinlineit.ctrlf.repository.network.api.UserApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://testdeploy-dev.ap-northeast-2.elasticbeanstalk.com/api/"
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
    .build()

object RegistrationService {
    val USER_API: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }
}

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
