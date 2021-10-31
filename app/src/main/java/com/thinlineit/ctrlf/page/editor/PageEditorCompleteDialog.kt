package com.thinlineit.ctrlf.page.editor

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.util.CustomDialogInterface
import kotlinx.android.synthetic.main.dialog_create_issue.*

class PageEditorCompleteDialog(context: Context, Interface: CustomDialogInterface) :
    Dialog(context) {
    private var customDialogInterface: CustomDialogInterface = Interface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_create_issue)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        confirmButton.setOnClickListener {
            dismiss()
            customDialogInterface.onFinishButton()
        }
    }
}
