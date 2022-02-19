package com.thinlineit.ctrlf.page.detail

import android.content.Context
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.util.CustomDialog

class NoteDeleteClickListener(
    val context: Context,
    private val pageViewModel: PageViewModel
) {
    private var curDialog: CustomDialog? = null

    fun onDelete() {
        curDialog = CustomDialog(context, pageViewModel.curNoteId.value ?: return).apply {
            title = context.resources.getString(R.string.label_delete_note)
            contentBodyHint = context.resources.getString(R.string.hint_dialog_delete)
            confirmButtonText = context.resources.getString(R.string.button_dialog_confirm)
            dismissButtonText = context.resources.getString(R.string.button_dialog_cancel)
            confirmClickListener = ::onDeleteConfirmClick
            dismissClickListener = ::onDeleteDismissClick
        }
        curDialog?.show()
    }

    private fun onDeleteConfirmClick(id: Int, title: String, contentText: String) {
        pageViewModel.deleteNote(contentText, id)
        curDialog?.dismiss()
        curDialog = CustomDialog(context, id).apply {
            bodyText = context.resources.getString(R.string.label_complete_delete_issue)
            dismissButtonText =
                context.resources.getString(R.string.button_dialog_confirm)
            dismissClickListener = ::onDeleteDismissClick
        }
        curDialog?.show()
    }

    private fun onDeleteDismissClick() {
        curDialog?.dismiss()
    }
}
