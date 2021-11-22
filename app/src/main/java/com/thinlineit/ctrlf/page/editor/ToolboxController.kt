package com.thinlineit.ctrlf.page.editor

import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.thinlineit.ctrlf.R

class ToolboxController(private val viewGroup: ViewGroup) {
    var toolboxEventListener: ToolboxEventListener? = null

    var isActive: Boolean = false
        set(value) {
            field = value
            toggleVisible()
        }

    init {
        viewGroup.findViewById<ImageButton>(R.id.boldText).setOnClickListener {
            toolboxEventListener?.boldText()
        }
        viewGroup.findViewById<ImageButton>(R.id.headerText).setOnClickListener {
            toolboxEventListener?.headerText()
        }
        viewGroup.findViewById<ImageButton>(R.id.italicText).setOnClickListener {
            toolboxEventListener?.italicText()
        }
        viewGroup.findViewById<ImageButton>(R.id.quoteText).setOnClickListener {
            toolboxEventListener?.quoteText()
        }
        viewGroup.findViewById<ImageButton>(R.id.codeText).setOnClickListener {
            toolboxEventListener?.codeText()
        }
        viewGroup.findViewById<ImageButton>(R.id.bulletedList).setOnClickListener {
            toolboxEventListener?.bulletedList()
        }
        viewGroup.findViewById<ImageButton>(R.id.numberList).setOnClickListener {
            toolboxEventListener?.numberList()
        }
        toggleVisible()
    }

    private fun visible() {
        viewGroup.visibility = View.VISIBLE
    }

    private fun invisible() {
        viewGroup.visibility = View.GONE
    }

    private fun toggleVisible() {
        if (isActive) visible() else invisible()
    }
}

interface ToolboxEventListener {
    fun boldText()
    fun headerText()
    fun italicText()
    fun quoteText()
    fun codeText()
    fun linkText()
    fun bulletedList()
    fun numberList()
    fun attachImage()
}

