package com.thinlineit.ctrlf.repository.dao

import com.thinlineit.ctrlf.entity.User
import com.thinlineit.ctrlf.repository.dto.request.AuthEmailRequest
import com.thinlineit.ctrlf.repository.dto.request.CodeCheckRequest
import com.thinlineit.ctrlf.repository.dto.request.ResetPasswordRequest
import com.thinlineit.ctrlf.repository.dto.request.SignUpRequest
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

    suspend fun checkCode(code: String, signingToken: String): Boolean =
        try {
            val codeResponse =
                RegistrationService.USER_API.requestCodeCheck(CodeCheckRequest(code, signingToken))
            Application.preferenceUtil.setString(SIGNING_TOKEN_CODE, codeResponse.signingToken)
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
            val emailResponse =
                RegistrationService.USER_API.sendingAuthEmail(AuthEmailRequest(email))
            Application.preferenceUtil.setString(SIGNING_TOKEN, emailResponse.signingToken)
            true
        } catch (e: Exception) {
            false
        }

    fun returnSigningToken(): String {
        return Application.preferenceUtil.getString(
            SIGNING_TOKEN, ""
        )
    }

    fun returnCodeSigningToken(): String {
        return Application.preferenceUtil.getString(
            SIGNING_TOKEN_CODE, ""
        )
    }

    suspend fun requestSignUp(
        nickName: String,
        password: String,
        passwordConfirm: String,
        signingToken: String
    ): Boolean =
        try {
            RegistrationService.USER_API.requestSignUp(
                SignUpRequest(nickName, password, passwordConfirm, signingToken)
            )
            true
        } catch (e: Exception) {
            false
        }

    suspend fun requestResetPassword(
        password: String,
        passwordConfirm: String,
        signingToken: String
    ): Boolean =
        try {
            RegistrationService.USER_API.resetPassword(
                ResetPasswordRequest(password, passwordConfirm, signingToken)
            )
            true
        } catch (e: Exception) {
            false
        }

    companion object {
        private const val TOKEN = "token"
        private const val SIGNING_TOKEN = "signing token"
        private const val SIGNING_TOKEN_CODE = "code signing token"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
    }
}
