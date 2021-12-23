package com.thinlineit.ctrlf.registration.signup

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {
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
    val password = MutableLiveData("")
    val passwordConfirm = MutableLiveData("")
    val nickName = MutableLiveData("")
    val code = MutableLiveData("")

    val countTimerStatus = MutableLiveData<Event<Timer>>()
    val emailStatus = MutableLiveData<Event<Status>>()
    val codeStatus = MutableLiveData<Event<Status>>()
    val nicknameStatus = MutableLiveData<Event<Status>>()
    val passwordStatus = MutableLiveData<Event<Status>>()
    val passwordConfirmStatus = MutableLiveData<Event<Status>>()
    val registerClick = MutableLiveData<Event<Boolean>>()

    val registrationStatus = MediatorLiveData<Boolean>().apply {
        addSourceList(
            emailStatus,
            codeStatus,
            nicknameStatus,
            passwordStatus,
            passwordConfirmStatus
        ) {
            value = isSignUpValid()
        }
    }

    val emailMessage = MutableLiveData<Int>(R.string.empty_text)
    val nicknameMessage = MutableLiveData<Int>(R.string.empty_text)
    val passwordMessage = MutableLiveData<Int>(R.string.empty_text)
    val passwordConfirmMessage = MutableLiveData<Int>(R.string.empty_text)
    val codeMessage = MutableLiveData<Int>(R.string.empty_text)

    val emailInvoke: () -> Unit = this::checkDuplicateEmail
    val codeInvoke: () -> Unit = this::checkCodeValid
    val nicknameInvoke: () -> Unit = this::checkDuplicateNickname
    val passwordInvoke: () -> Unit = this::checkPasswordValid
    val passwordConfirmInvoke: () -> Unit = this::checkPasswordSame

    val countText = MutableLiveData<String>("")

    fun checkPasswordSame() {
        if (!passwordConfirm.value.isValid(PASSWORD_REGEX)) {
            passwordConfirmStatus.value = Event(Status.FAILURE)
            passwordConfirmMessage.postValue(R.string.notice_error_password)
            return
        }
        if (password.value == passwordConfirm.value) {
            passwordConfirmStatus.postEvent(Status.SUCCESS)
            passwordConfirmMessage.postValue(R.string.empty_text)
        } else {
            passwordConfirmMessage.postValue(R.string.notice_error_password)
            passwordConfirmStatus.postEvent(Status.FAILURE)
        }
    }

    private fun isSignUpValid(): Boolean =
        emailStatus.value?.equalContent(Status.SUCCESS) ?: false &&
            codeStatus.value?.equalContent(Status.SUCCESS) ?: false &&
            nicknameStatus.value?.equalContent(Status.SUCCESS) ?: false &&
            passwordStatus.value?.equalContent(Status.SUCCESS) ?: false &&
            passwordConfirmStatus.value?.equalContent(Status.SUCCESS) ?: false

    fun checkDuplicateNickname() {
        val nickname = nickName.value.takeIf {
            it.isValid(NICKNAME_REGEX)
        } ?: run {
            nicknameMessage.postValue(R.string.notice_nickname_valid_condition)
            nicknameStatus.postEvent(Status.FAILURE)
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            if (userRepository.checkNickname(nickname)) {
                nicknameStatus.postEvent(Status.SUCCESS)
                nicknameMessage.postValue(R.string.empty_text)
            } else {
                nicknameStatus.postEvent(Status.FAILURE)
                nicknameMessage.postValue(R.string.notice_exist_nickname)
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
            } else {
                codeMessage.postValue(R.string.notice_code_is_not_valid)
                codeStatus.value = Event(Status.FAILURE)
            }
        }
    }

    fun checkPasswordValid() {
        if (password.value.isValid(PASSWORD_REGEX)) {
            passwordStatus.value = Event(Status.SUCCESS)
            passwordMessage.postValue(R.string.empty_text)
        } else {
            passwordMessage.postValue(R.string.notice_password_valid_condition)
            passwordStatus.value = Event(Status.FAILURE)
        }
    }

    fun checkDuplicateEmail() {
        val email = email.value.takeIf { it.isValid(EMAIL_REGEX) } ?: run {
            emailStatus.postEvent(Status.FAILURE)
            emailMessage.postValue(R.string.notice_error_email)
            return
        }
        viewModelScope.launch {
            if (userRepository.isEmailExist(email)) {
                emailStatus.postEvent(Status.FAILURE)
                emailMessage.postValue(R.string.notice_exist_email)
            } else {
                emailMessage.postValue(R.string.empty_text)
                emailStatus.postEvent(Status.SUCCESS)
                sendAuthEmail()
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

    fun requestSignUp() {
        val nickname = nickName.value ?: return
        val password = password.value ?: return
        val passwordConfirm = passwordConfirm.value ?: return
        val signingToken = userRepository.returnCodeSigningToken()

        viewModelScope.launch {
            if (userRepository.requestSignUp(
                    nickname,
                    password,
                    passwordConfirm,
                    signingToken
                )
            ) {
                registerClick.postValue(Event(true))
            }
        }
    }

    fun resetEmailCodeValue() {
        email.value = ""
        code.value = ""
    }

    companion object {
        // 숫자, 문자, 특수문자 중 2가지 포함(8~20자)
        private const val PASSWORD_REGEX =
            "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,20}\$"
        private const val EMAIL_REGEX = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,8}$"
        private const val NICKNAME_REGEX = "^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ]{2,10}$"
        private const val CODE_REGEX = "^[a-zA-Z0-9]{8}$"
    }
}
