package com.thinlineit.ctrlf.util.base

import androidx.lifecycle.MutableLiveData

class ListLiveData<T> : MutableLiveData<List<T>>() {
    private val temp = mutableListOf<T>()

    init {
        value = temp
    }

    fun add(item: T) {
        temp.add(item)
        value = temp
    }

    fun removeAt(position: Int) {
        temp.removeAt(position)
        value = temp
    }

    fun clear() {
        temp.clear()
        value = temp
    }
}
