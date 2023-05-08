package com.thinlineit.ctrlf.util

interface BindingRecyclerViewAdapter<T> {
    fun setData(data: T)
}

interface BindingFragmentStateAdapter<T> {
    fun setData(data: T)
}
