package com.thinlineit.ctrlf.registration.signin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.model.repository.UserRepository
import com.thinlineit.ctrlf.util.Event
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.isValid
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val loginStatus = MutableLiveData<Event<Status>>()
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val loginMessage = MutableLiveData<Int>(R.string.empty_text)

    private fun login() {
        viewModelScope.launch {
            if (userRepository.logIn(email.value.toString(), password.value.toString())) {
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

    class LoginViewModelFactory(
        private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(userRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object {
        private const val EMAILREGEX = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,8}$"
    }
}
