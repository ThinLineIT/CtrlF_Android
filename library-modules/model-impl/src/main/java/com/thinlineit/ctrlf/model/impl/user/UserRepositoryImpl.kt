package com.thinlineit.ctrlf.model.impl.user

import android.content.Context
import com.google.auto.service.AutoService
import com.linecorp.lich.component.ServiceLoaderComponent
import com.thinlineit.ctrlf.model.data.User
import com.thinlineit.ctrlf.model.impl.retrofit
import com.thinlineit.ctrlf.model.impl.user.local.LocalUserDataSource
import com.thinlineit.ctrlf.model.impl.user.remote.RemoteUserDataSource
import com.thinlineit.ctrlf.model.impl.user.remote.dto.request.AuthEmailRequest
import com.thinlineit.ctrlf.model.impl.user.remote.dto.request.CodeCheckRequest
import com.thinlineit.ctrlf.model.impl.user.remote.dto.request.ResetPasswordRequest
import com.thinlineit.ctrlf.model.impl.user.remote.dto.request.SignUpRequest
import com.thinlineit.ctrlf.model.repository.UserRepository

@AutoService(UserRepository::class)
class UserRepositoryImpl : UserRepository, ServiceLoaderComponent {
    private lateinit var localDataSource: LocalUserDataSource
    private val remoteDataSource: RemoteUserDataSource by lazy {
        retrofit.create(RemoteUserDataSource::class.java)
    }

    override fun init(context: Context) {
        localDataSource = LocalUserDataSource(context.applicationContext)
    }

    // Log in
    override suspend fun logIn(email: String, password: String): Boolean = try {
        val loginResponse = remoteDataSource.login(User(email, password))
        localDataSource.setToken(loginResponse.token)
        localDataSource.setEmail(email)
        localDataSource.setPassword(password)
        true
    } catch (e: Exception) {
        false
    }

    override suspend fun autoLogIn(): Boolean {
        val isAutoLogin = localDataSource.getToken() != null
        return if (isAutoLogin) {
            val email = localDataSource.getEmail() ?: return false
            val password = localDataSource.getPassword() ?: return false
            logIn(email, password)
        } else {
            false
        }
    }

    // Log out
    override fun logOut() {
        localDataSource.setToken(null)
        localDataSource.setEmail(null)
        localDataSource.setPassword(null)
    }

    // Registration
    override suspend fun checkNickname(nickName: String): Boolean = try {
        remoteDataSource.checkNickname(nickName)
        true
    } catch (e: Exception) {
        false
    }

    override suspend fun sendAuthCode(email: String): Boolean = try {
        val emailResponse = remoteDataSource.sendAuthEmail(AuthEmailRequest(email))
        localDataSource.setSigningToken(emailResponse.signingToken)
        true
    } catch (e: Exception) {
        false
    }

    override suspend fun checkCode(code: String, signingToken: String): Boolean = try {
        val codeCheckResponse = remoteDataSource.checkVerificationCode(
            CodeCheckRequest(code, signingToken)
        )
        localDataSource.setSigningToken(codeCheckResponse.signingToken)
        true
    } catch (e: Exception) {
        false
    }

    override suspend fun isEmailExist(email: String): Boolean = try {
        remoteDataSource.checkEmail(email)
        false
    } catch (e: Exception) {
        true
    }

    override fun getSigningToken(): String? = localDataSource.getSigningToken()

    override suspend fun signUp(
        nickName: String,
        password: String,
        passwordConfirm: String,
        signingToken: String
    ): Boolean = try {
        remoteDataSource.signUp(
            SignUpRequest(
                nickName,
                password,
                passwordConfirm,
                signingToken
            )
        )
        true
    } catch (e: Exception) {
        false
    }

    // Find Password
    override suspend fun resetPassword(
        password: String,
        passwordConfirm: String,
        signingToken: String
    ): Boolean = try {
        remoteDataSource.resetPassword(
            ResetPasswordRequest(
                password,
                passwordConfirm,
                signingToken
            )
        )
        true
    } catch (e: Exception) {
        false
    }
}
