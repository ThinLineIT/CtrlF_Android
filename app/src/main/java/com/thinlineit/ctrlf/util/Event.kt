package com.thinlineit.ctrlf.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

class Event<T>(private val content: T) {
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content

    @Suppress("UNCHECKED_CAST")
    fun equalContent(other: Any?): Boolean {
        return (other as T) == this.peekContent()
    }
}

fun <T> LiveData<Event<T>>.observeIfNotHandled(owner: LifecycleOwner, onChanged: (T) -> Unit) {
    observe(owner) {
        it.getContentIfNotHandled()?.let { value ->
            onChanged(value)
        }
    }
}

enum class Status {
    SUCCESS,
    FAILURE;
}

enum class Timer {
    FINISH,
    EXECUTE;
}
