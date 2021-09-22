package com.thinlineit.ctrlf.page

import android.content.Context

interface ItemTouchHelperListener {
    fun onModify(context: Context)
    fun onDelete(context: Context)
}
