package com.thinlineit.ctrlf.page.editor

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.thinlineit.ctrlf.util.CustomDialogInterface
import kotlinx.android.synthetic.main.activity_page_editor.*
import kotlinx.android.synthetic.main.dialog_create_issue.confirmButton

class PageEditorDialog(
    context: Context,
    private val customDialogInterface: CustomDialogInterface,
    private val resID: Int
) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(resID)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        confirmButton.setOnClickListener {
            dismiss()
            customDialogInterface.onFinishButton()
        }
        cancelButton.setOnClickListener {
            dismiss()
        }
    }
}
