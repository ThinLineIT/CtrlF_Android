package com.thinlineit.ctrlf.page.detail

import android.widget.LinearLayout
import android.widget.TextView

interface SwipeInterface {
    fun getSwipeWidth(): Int
    fun getSwipeLayout(): LinearLayout
    fun getUpdateButton(): TextView
    fun getDeleteButton(): TextView
}
