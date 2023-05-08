package com.thinlineit.ctrlf.repository.network.api

import com.thinlineit.ctrlf.entity.User
import com.thinlineit.ctrlf.repository.dto.request.AuthEmailRequest
import com.thinlineit.ctrlf.repository.dto.request.CodeCheckRequest
import com.thinlineit.ctrlf.repository.dto.request.ResetPasswordRequest
import com.thinlineit.ctrlf.repository.dto.request.SignUpRequest
import com.thinlineit.ctrlf.repository.dto.response.AuthEmailResponse
import com.thinlineit.ctrlf.repository.dto.response.CodeCheckResponse
import com.thinlineit.ctrlf.repository.dto.response.EmailCheckResponse
import com.thinlineit.ctrlf.repository.dto.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {

    @POST("auth/login/")
    suspend fun requestLogin(
        @Body request: User
    ): LoginResponse

    @POST("auth/signup/")
    suspend fun requestSignUp(
        @Body request: SignUpRequest
    )

    @POST("auth/signup/email/")
    suspend fun sendingAuthEmail(
        @Body request: AuthEmailRequest
    ): AuthEmailResponse

    @POST("auth/verification-code/check/")
    suspend fun requestCodeCheck(
        @Body request: CodeCheckRequest
    ): CodeCheckResponse

    @POST("auth/reset_password/")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    )

    @GET("auth/signup/nickname/duplicate/")
    suspend fun checkNickname(@Query("data") data: String): EmailCheckResponse

    @GET("auth/signup/email/duplicate/")
    suspend fun checkEmail(@Query("data") data: String): EmailCheckResponse
}
