package com.thinlineit.ctrlf.page.detail

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import com.thinlineit.ctrlf.R
import kotlinx.android.synthetic.main.fragment_create_dialog.cancelDialogButton
import kotlinx.android.synthetic.main.fragment_create_dialog.createDialog
import kotlinx.android.synthetic.main.fragment_create_dialog.createDialogButton
import kotlinx.android.synthetic.main.fragment_create_dialog.createDialogReason
import kotlinx.android.synthetic.main.fragment_create_dialog.createDialogTitle

class CreateDialog(
    context: Context,
    private val titleText: String,
    private val titleHint: String,
    private val onCreateClicked: (title: String, reason: String) -> Unit
) {
    private val dialog = Dialog(context)
    fun openDialog() {

        dialog.apply {
            setContentView(R.layout.fragment_create_dialog)
            window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            createDialog.text = titleText
            createDialogTitle.hint = titleHint

            setCanceledOnTouchOutside(true)
            setCancelable(true)
            show()

            createDialogButton.setOnClickListener {
                val title = dialog.createDialogTitle.text.toString()
                val reason = dialog.createDialogReason.text.toString()
                onCreateClicked(title, reason)
                dismiss()
            }
            cancelDialogButton.setOnClickListener {
                dismiss()
            }
        }
    }
}
