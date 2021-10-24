package com.thinlineit.ctrlf.page.detail

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.thinlineit.ctrlf.R

class FloatingMenuUIController(
    activity: AppCompatActivity,
    isFabOpen: LiveData<Boolean>,
    private val floatingButton: FloatingActionButton,
    private val floatingMenuLayout: View
) {
    private val context = activity.baseContext

    init {
        isFabOpen.observe(activity) {
            toggleFAB(it)
        }
    }

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            context, R.anim.fab_rotate_open
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.fab_rotate_close
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.fab_from_bottom
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.fab_to_bottom
        )
    }

    fun toggleFAB(isFabOpen: Boolean) {
        if (isFabOpen) {
            floatingButton.startAnimation(rotateOpen)
            floatingMenuLayout.startAnimation(fromBottom)
        } else {
            floatingButton.startAnimation(rotateClose)
            floatingMenuLayout.startAnimation(toBottom)
        }
    }
}
