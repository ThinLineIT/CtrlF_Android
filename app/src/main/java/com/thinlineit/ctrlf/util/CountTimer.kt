package com.thinlineit.ctrlf.util

import android.os.CountDownTimer

class CountTimer(private val countTimerLister: CountTimerListener) :
    CountDownTimer(AUTH_CODE_VALID_TIME_LIMIT, UNIT_MILLIS_PER_SEC) {

    override fun onTick(millisUntilFinished: Long) {
        val minutes = (millisUntilFinished / UNIT_MILLIS_PER_SEC) / UNIT_SEC_PER_MIN
        val seconds = (millisUntilFinished / UNIT_MILLIS_PER_SEC) % UNIT_SEC_PER_MIN
        val min = String.format("%02d", minutes)
        val sec = String.format("%02d", seconds)
        val time = "$min:$sec"

        countTimerLister.onTick(time)
    }

    override fun onFinish() {
        countTimerLister.onFinish()
    }

    companion object {
        private const val AUTH_CODE_VALID_TIME_LIMIT: Long = 180000
        private const val UNIT_MILLIS_PER_SEC: Long = 1000
        private const val UNIT_SEC_PER_MIN = 60
    }
}

interface CountTimerListener {
    fun onTick(time: String)
    fun onFinish()
}
