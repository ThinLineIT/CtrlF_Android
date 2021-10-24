package com.thinlineit.ctrlf.page

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.thinlineit.ctrlf.R
import kotlinx.android.synthetic.main.dialog_create_issue.*

class PageEditorCompleteDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_create_issue)

        confirmButton.setOnClickListener {
            PageActivity.start(context)
        }
    }
}
