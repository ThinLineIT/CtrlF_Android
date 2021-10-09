package com.thinlineit.ctrlf.login

import android.os.CountDownTimer
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.repository.UserRepository
import com.thinlineit.ctrlf.util.Event
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.addSourceList
import com.thinlineit.ctrlf.util.isValid
import com.thinlineit.ctrlf.util.postEvent
import kotlinx.coroutines.launch

class FindPasswordViewModel : ViewModel() {
    private val userRepository = UserRepository()

    val email = MutableLiveData("")
    val code = MutableLiveData("")
    val password = MutableLiveData("")
    val passwordConfirm = MutableLiveData("")

    val emailMessage = MutableLiveData<Int>(R.string.empty_text)
    val passwordMessage = MutableLiveData<Int>(R.string.empty_text)
    val passwordConfirmMessage = MutableLiveData<Int>(R.string.empty_text)
    val codeMessage = MutableLiveData<Int>(R.string.empty_text)

    val emailInvoke: () -> Unit = this::checkEmailPresence
    val codeInvoke: () -> Unit = this::checkCodeValid
    val passwordInvoke: () -> Unit = this::checkPasswordValid
    val passwordConfirmInvoke: () -> Unit = this::checkPasswordSame

    val emailStatus = MutableLiveData<Event<Status>>()
    val codeStatus = MutableLiveData<Event<Status>>()
    val passwordStatus = MutableLiveData<Event<Status>>()
    val passwordConfirmStatus = MutableLiveData<Event<Status>>()
    val completeClick = MutableLiveData<Event<Boolean>>()

    val countText = MutableLiveData("")

    val liveDataMerger = MediatorLiveData<Boolean>().apply {
        addSourceList(
            emailStatus,
            codeStatus,
            passwordStatus,
            passwordConfirmStatus
        ) {
            isPasswordResetValid()
        }
    }

    fun checkEmailPresence() {
        if (!email.value.isValid(EMAIL_REGEX)) {
            emailMessage.postValue(R.string.notice_error_email)
            emailStatus.postEvent(Status.FAILURE)
            return
        }
        viewModelScope.launch {
            if (userRepository.isEmailExist(email.toString())) {
                emailMessage.postValue(R.string.empty_text)
                emailStatus.postEvent(Status.SUCCESS)
                sendAuthEmail()
            } else {
                emailStatus.postEvent(Status.FAILURE)
                emailMessage.postValue(R.string.notice_non_exist_email)
            }
        }
    }

    private fun sendAuthEmail() {
        viewModelScope.launch {
            if (userRepository.sendAuthCode(email.value.toString())) {
                emailStatus.postEvent(Status.SUCCESS)
                emailMessage.postValue(R.string.empty_text)
                countTimer.start()
            }
        }
    }

    fun checkCodeValid() {
        if (code.value.toString().isEmpty()) {
            codeStatus.value = Event(Status.FAILURE)
            codeMessage.postValue(R.string.notice_enter_code)
            return
        }
        if (!code.value.isValid(CODE_REGEX)) {
            codeMessage.postValue(R.string.notice_check_code)
            codeStatus.value = Event(Status.FAILURE)
            return
        }
        viewModelScope.launch {
            if (userRepository.checkCode(code.value.toString())) {
                codeStatus.postEvent(Status.SUCCESS)
                codeMessage.postValue(R.string.empty_text)
                countTimer.onFinish()
            } else {
                codeMessage.postValue(R.string.notice_code_is_not_valid)
                codeStatus.value = Event(Status.FAILURE)
            }
        }
    }

    fun resendCode() {
        sendAuthEmail()
        countTimer.start()
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

    private val countTimer = object : CountDownTimer(180000, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            val minutes = (millisUntilFinished / 1000) / 60
            val seconds = (millisUntilFinished / 1000) % 60
            val min = String.format("%02d", minutes)
            val sec = String.format("%02d", seconds)

            countText.value = "$min:$sec"
        }

        override fun onFinish() {
            countText.postValue(DEFAULT_TIMER)
        }
    }

    private fun isPasswordResetValid(): Boolean =
        emailStatus.value?.equalContent(Status.SUCCESS) ?: false &&
            codeStatus.value?.equalContent(Status.SUCCESS) ?: false &&
            passwordStatus.value?.equalContent(Status.SUCCESS) ?: false &&
            passwordConfirmStatus.value?.equalContent(Status.SUCCESS) ?: false

    fun requestResetPassword() {
        viewModelScope.launch {
            if (userRepository.requestResetPassword(
                    password.value.toString(),
                    passwordConfirm.value.toString()
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
        private const val DEFAULT_TIMER = "인증번호를 재발급 받으세요."
    }
}
