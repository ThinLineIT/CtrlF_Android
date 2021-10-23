package com.thinlineit.ctrlf.registration.signin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.repository.dao.UserRepository
import com.thinlineit.ctrlf.util.Event
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.isValid
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val userRepository: UserRepository by lazy {
        UserRepository()
    }

    val loginStatus = MutableLiveData<Event<Status>>()
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val loginMessage = MutableLiveData<Int>(R.string.empty_text)

    private fun login() {
        viewModelScope.launch {
            if (userRepository.doLogin(email.value.toString(), password.value.toString())) {
                loginStatus.postValue(Event(Status.SUCCESS))
            } else {
                loginMessage.postValue(R.string.notice_check_email_password)
                loginStatus.postValue(Event(Status.FAILURE))
            }
        }
    }

    fun checkLogin() {
        val emailValue = email.value ?: ""
        val passwordValue = password.value ?: ""

        if (emailValue == "" || passwordValue == "") {
            loginMessage.postValue(R.string.notice_input_email_password)
        } else if (!emailValue.isValid(EMAILREGEX)) {
            loginMessage.postValue(R.string.notice_error_email)
        } else {
            login()
        }
    }

    companion object {
        private const val EMAILREGEX = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,8}$"
    }
}
