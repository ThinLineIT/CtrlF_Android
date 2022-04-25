package com.thinlineit.ctrlf.model.repository

import android.content.Context
import com.linecorp.lich.component.ComponentFactory

interface UserRepository {

    suspend fun logIn(email: String, password: String): Boolean

    suspend fun autoLogIn(): Boolean

    suspend fun checkNickname(nickName: String): Boolean

    suspend fun checkCode(code: String, signingToken: String): Boolean

    suspend fun isEmailExist(email: String): Boolean

    suspend fun sendAuthCode(email: String): Boolean

    fun getSigningToken(): String?

    suspend fun signUp(
        nickName: String,
        password: String,
        passwordConfirm: String,
        signingToken: String
    ): Boolean

    suspend fun resetPassword(
        password: String,
        passwordConfirm: String,
        signingToken: String
    ): Boolean

    fun logOut()

    companion object : ComponentFactory<UserRepository>() {
        override fun createComponent(context: Context): UserRepository =
            UserRepository.delegateToServiceLoader(context)
    }
}
