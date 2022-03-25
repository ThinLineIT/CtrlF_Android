package com.thinlineit.ctrlf.model.impl.user.local

import android.content.Context
import com.thinlineit.ctrlf.model.impl.PreferenceUtil

class LocalUserDataSource(context: Context) {
    private val userInfoPreference = PreferenceUtil(context, USER_INFO)

    fun setToken(token: String?) = userInfoPreference.setString(TOKEN, token)

    fun getToken(): String? = userInfoPreference.getString(TOKEN)

    fun setEmail(email: String?) = userInfoPreference.setString(EMAIL, email)

    fun getEmail(): String? = userInfoPreference.getString(EMAIL)

    fun setPassword(password: String?) = userInfoPreference.setString(PASSWORD, password)

    fun getPassword(): String? = userInfoPreference.getString(PASSWORD)

    fun setSigningToken(signingToken: String) =
        userInfoPreference.setString(SIGNING_TOKEN, signingToken)

    fun getSigningToken(): String? = userInfoPreference.getString(SIGNING_TOKEN)

    companion object {
        private const val TOKEN = "token"
        private const val SIGNING_TOKEN = "signing token"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
        private const val USER_INFO = "UserInfo"
    }
}
