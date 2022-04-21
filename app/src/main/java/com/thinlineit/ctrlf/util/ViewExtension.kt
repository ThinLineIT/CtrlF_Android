package com.thinlineit.ctrlf.util

import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible

fun View.setBackgroundById(id: Int) {
    background = AppCompatResources.getDrawable(this.context, id)
}

fun View.changeVisibilityState() {
    this.visibility =
        if (this.isVisible) View.INVISIBLE
        else View.VISIBLE
}
