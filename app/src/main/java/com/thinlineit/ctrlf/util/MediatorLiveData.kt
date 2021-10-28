package com.thinlineit.ctrlf.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T> MediatorLiveData<T>.addSourceList(
    vararg liveDataArgument: LiveData<*>,
    onChange: () -> T
) {
    liveDataArgument.forEach {
        this.addSource(it) { value = onChange() }
    }
}
