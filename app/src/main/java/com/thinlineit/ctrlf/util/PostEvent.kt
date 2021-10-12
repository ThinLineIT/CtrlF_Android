package com.thinlineit.ctrlf.util

import androidx.lifecycle.MutableLiveData

fun MutableLiveData<Event<Status>>.postEvent(status: Status) {
    this.postValue(Event(status))
}

fun MutableLiveData<Event<Timer>>.postTimerEvent(status: Timer) {
    this.postValue(Event(status))
}
