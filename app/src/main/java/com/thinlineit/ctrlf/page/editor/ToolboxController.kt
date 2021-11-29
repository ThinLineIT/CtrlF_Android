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
        viewGroup.findViewById<ImageButton>(R.id.boldTextTool).setOnClickListener {
            toolboxEventListener?.boldText()
        }
        viewGroup.findViewById<ImageButton>(R.id.headerTextTool).setOnClickListener {
            toolboxEventListener?.headerText()
        }
        viewGroup.findViewById<ImageButton>(R.id.italicTextTool).setOnClickListener {
            toolboxEventListener?.italicText()
        }
        viewGroup.findViewById<ImageButton>(R.id.quoteTextTool).setOnClickListener {
            toolboxEventListener?.quoteText()
        }
        viewGroup.findViewById<ImageButton>(R.id.codeTextTool).setOnClickListener {
            toolboxEventListener?.codeText()
        }
        viewGroup.findViewById<ImageButton>(R.id.bulletedListTool).setOnClickListener {
            toolboxEventListener?.bulletedList()
        }
        viewGroup.findViewById<ImageButton>(R.id.numberListTool).setOnClickListener {
            toolboxEventListener?.numberList()
        }
        viewGroup.findViewById<ImageButton>(R.id.linkTool).setOnClickListener {
            toolboxEventListener?.linkText()
        }
        viewGroup.findViewById<ImageButton>(R.id.imageTool).setOnClickListener {
            toolboxEventListener?.attachImage()
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
