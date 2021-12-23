package com.thinlineit.ctrlf.registration.signin

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.repository.dao.UserRepository
import com.thinlineit.ctrlf.util.CountTimer
import com.thinlineit.ctrlf.util.CountTimerListener
import com.thinlineit.ctrlf.util.Event
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.Timer
import com.thinlineit.ctrlf.util.addSourceList
import com.thinlineit.ctrlf.util.isValid
import com.thinlineit.ctrlf.util.postEvent
import com.thinlineit.ctrlf.util.postTimerEvent
import kotlinx.coroutines.launch

class FindPasswordViewModel : ViewModel() {
    private val userRepository = UserRepository()
    private val countTimer = CountTimer(object : CountTimerListener {
        override fun onTick(time: String) {
            countText.value = time
        }

        override fun onFinish() {
            countTimerStatus.postTimerEvent(Timer.FINISH)
        }
    })

    val email = MutableLiveData("")
    val code = MutableLiveData("")
    val password = MutableLiveData("")
    val passwordConfirm = MutableLiveData("")

    val emailMessage = MutableLiveData<Int>(R.string.empty_text)
    val passwordMessage = MutableLiveData<Int>(R.string.empty_text)
    val passwordConfirmMessage = MutableLiveData<Int>(R.string.empty_text)
    val codeMessage = MutableLiveData<Int>(R.string.empty_text)

    val emailInvoke: () -> Unit = this::checkEmailExist
    val codeInvoke: () -> Unit = this::checkCodeValid
    val passwordInvoke: () -> Unit = this::checkPasswordValid
    val passwordConfirmInvoke: () -> Unit = this::checkPasswordSame

    val emailStatus = MutableLiveData<Event<Status>>()
    val codeStatus = MutableLiveData<Event<Status>>()
    val passwordStatus = MutableLiveData<Event<Status>>()
    val passwordConfirmStatus = MutableLiveData<Event<Status>>()
    val completeClick = MutableLiveData<Event<Boolean>>()
    val countTimerStatus = MutableLiveData<Event<Timer>>()
    val countText = MutableLiveData<String>("")

    val passwordResetStatus = MediatorLiveData<Boolean>().apply {
        addSourceList(
            emailStatus,
            codeStatus,
            passwordStatus,
            passwordConfirmStatus
        ) {
            value = isPasswordResetValid()
        }
    }

    fun checkEmailExist() {
        val email = email.value.takeIf {
            it.isValid(EMAIL_REGEX)
        } ?: run {
            emailMessage.postValue(R.string.notice_error_email)
            emailStatus.postEvent(Status.FAILURE)
            return
        }
        viewModelScope.launch {
            if (userRepository.isEmailExist(email)) {
                emailMessage.postValue(R.string.empty_text)
                emailStatus.postEvent(Status.SUCCESS)
                sendAuthEmail()
            } else {
                emailStatus.postEvent(Status.FAILURE)
                emailMessage.postValue(R.string.notice_non_exist_email)
            }
        }
    }

    fun sendAuthEmail() {
        val email = email.value ?: return
        viewModelScope.launch {
            if (userRepository.sendAuthCode(email)) {
                emailMessage.postValue(R.string.empty_text)
                countTimer.start()
                countTimerStatus.postTimerEvent(Timer.EXECUTE)
            }
        }
    }

    fun checkCodeValid() {
        if (code.value.toString().isEmpty()) {
            codeStatus.value = Event(Status.FAILURE)
            codeMessage.postValue(R.string.notice_enter_code)
            return
        }
        val code = code.value.takeIf {
            it.isValid(CODE_REGEX)
        } ?: run {
            codeMessage.postValue(R.string.notice_check_code)
            codeStatus.value = Event(Status.FAILURE)
            return
        }
        val signingToken = userRepository.returnSigningToken()
        viewModelScope.launch {
            if (userRepository.checkCode(code, signingToken)) {
                codeStatus.postEvent(Status.SUCCESS)
                codeMessage.postValue(R.string.empty_text)
                countTimer.onFinish()
            } else {
                codeMessage.postValue(R.string.notice_code_is_not_valid)
                codeStatus.value = Event(Status.FAILURE)
            }
        }
    }

    fun checkPasswordValid() {
        if (password.value.isValid(PASSWORD_REGEX)) {
            passwordMessage.postValue(R.string.empty_text)
            passwordStatus.postEvent(Status.SUCCESS)
        } else {
            passwordMessage.postValue(R.string.notice_password_valid_condition)
            passwordStatus.postEvent(Status.FAILURE)
        }
    }

    fun checkPasswordSame() {
        if (!passwordConfirm.value.isValid(PASSWORD_REGEX)) {
            passwordConfirmMessage.postValue(R.string.notice_error_password)
            passwordConfirmStatus.postEvent(Status.FAILURE)
            return
        }
        if (password.value == passwordConfirm.value) {
            passwordConfirmMessage.postValue(R.string.empty_text)
            passwordConfirmStatus.postEvent(Status.SUCCESS)
        } else {
            passwordConfirmMessage.postValue(R.string.notice_error_password)
            passwordConfirmStatus.postEvent(Status.FAILURE)
        }
    }

    private fun isPasswordResetValid(): Boolean =
        emailStatus.value?.equalContent(Status.SUCCESS) ?: false &&
            codeStatus.value?.equalContent(Status.SUCCESS) ?: false &&
            passwordStatus.value?.equalContent(Status.SUCCESS) ?: false &&
            passwordConfirmStatus.value?.equalContent(Status.SUCCESS) ?: false

    fun requestResetPassword() {
        val password = password.value ?: return
        val passwordConfirm = passwordConfirm.value ?: return
        val signingToken = userRepository.returnCodeSigningToken()

        viewModelScope.launch {
            if (userRepository.requestResetPassword(
                    password,
                    passwordConfirm,
                    signingToken
                )
            ) {
                completeClick.postValue(Event(true))
            }
        }
    }

    companion object {
        private const val PASSWORD_REGEX =
            "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,20}\$"
        private const val EMAIL_REGEX = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,8}$"
        private const val CODE_REGEX = "^[a-zA-Z0-9]{8}$"
    }
}
