package com.thinlineit.ctrlf.repository

import com.thinlineit.ctrlf.data.request.AuthEmailRequest
import com.thinlineit.ctrlf.data.request.CodeCheckRequest
import com.thinlineit.ctrlf.data.request.SignUpRequest
import com.thinlineit.ctrlf.model.User
import com.thinlineit.ctrlf.repository.network.RegistrationService
import com.thinlineit.ctrlf.util.Application

class UserRepository {

    suspend fun doLogin(email: String, password: String): Boolean =
        try {
            val loginResponse = RegistrationService.USER_API.requestLogin(
                User(email, password)
            )
            Application.preferenceUtil.setString(TOKEN, loginResponse.token)
            Application.preferenceUtil.setString(EMAIL, email)
            Application.preferenceUtil.setString(PASSWORD, password)
            true
        } catch (e: Exception) {
            false
        }

    suspend fun mayLogin(): Boolean =
        if (checkLogin()) {
            doLogin(
                Application.preferenceUtil.getString(EMAIL, ""),
                Application.preferenceUtil.getString(
                    PASSWORD, ""
                )
            )
        } else {
            false
        }

    private fun checkLogin(): Boolean {
        return Application.preferenceUtil.getString(EMAIL, "") != ""
    }

    suspend fun checkNickname(nickName: String): Boolean =
        try {
            RegistrationService.USER_API.checkNickname(nickName)
            true
        } catch (e: Exception) {
            false
        }

    suspend fun checkCode(code: String): Boolean =
        try {
            RegistrationService.USER_API.requestCodeCheck(CodeCheckRequest(code))
            true
        } catch (e: Exception) {
            false
        }

    suspend fun isEmailExist(email: String): Boolean =
        try {
            RegistrationService.USER_API.checkEmail(email)
            false
        } catch (e: Exception) {
            true
        }

    suspend fun sendAuthCode(email: String): Boolean =
        try {
            RegistrationService.USER_API.sendingAuthEmail(AuthEmailRequest(email))
            true
        } catch (e: Exception) {
            false
        }

    suspend fun requestSignUp(
        email: String,
        code: String,
        nickName: String,
        password: String,
        passwordConfirm: String
    ): Boolean =
        try {
            RegistrationService.USER_API.requestSignUp(
                SignUpRequest(email, code, nickName, password, passwordConfirm)
            )
            true
        } catch (e: Exception) {
            false
        }

    companion object {
        private const val TOKEN = "token"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
    }
}
