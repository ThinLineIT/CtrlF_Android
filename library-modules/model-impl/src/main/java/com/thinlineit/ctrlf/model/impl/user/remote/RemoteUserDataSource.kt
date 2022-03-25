package com.thinlineit.ctrlf.model.impl.user.remote

import com.thinlineit.ctrlf.model.data.User
import com.thinlineit.ctrlf.model.impl.user.remote.dto.request.AuthEmailRequest
import com.thinlineit.ctrlf.model.impl.user.remote.dto.request.CodeCheckRequest
import com.thinlineit.ctrlf.model.impl.user.remote.dto.request.ResetPasswordRequest
import com.thinlineit.ctrlf.model.impl.user.remote.dto.request.SignUpRequest
import com.thinlineit.ctrlf.model.impl.user.remote.dto.response.AuthEmailResponse
import com.thinlineit.ctrlf.model.impl.user.remote.dto.response.CodeCheckResponse
import com.thinlineit.ctrlf.model.impl.user.remote.dto.response.EmailCheckResponse
import com.thinlineit.ctrlf.model.impl.user.remote.dto.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RemoteUserDataSource {

    @POST("auth/login/")
    suspend fun login(
        @Body request: User
    ): LoginResponse

    @POST("auth/signup/")
    suspend fun signUp(
        @Body request: SignUpRequest
    )

    @POST("auth/signup/email/")
    suspend fun sendAuthEmail(
        @Body request: AuthEmailRequest
    ): AuthEmailResponse

    @POST("auth/verification-code/check/")
    suspend fun checkVerificationCode(
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
