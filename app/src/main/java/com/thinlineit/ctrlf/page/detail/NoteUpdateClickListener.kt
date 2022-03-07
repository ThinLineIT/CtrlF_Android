package com.thinlineit.ctrlf.page.detail

import android.content.Context
import android.widget.Toast
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.issue.IssueMaterial
import com.thinlineit.ctrlf.util.CustomDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteUpdateClickListener(
    private val context: Context,
    private val pageViewModel: PageViewModel
) {
    private var curDialog: CustomDialog? = null

    fun onUpdate() {
        curDialog = CustomDialog(context).apply {
            title = context.resources.getString(R.string.label_update_note)
            contentTitleHint =
                context.resources.getString(R.string.hint_dialog_note_title_hint_modify)
            contentBodyHint = context.resources.getString(R.string.label_summary)
            confirmButtonText = context.resources.getString(R.string.button_dialog_confirm)
            dismissButtonText = context.resources.getString(R.string.button_dialog_cancel)
            confirmClickListener = ::onUpdateConfirmClick
            dismissClickListener = ::onUpdateDismissClick
        }
        curDialog?.show()
    }

    private fun onUpdateConfirmClick(issueMaterial: IssueMaterial) {
        CoroutineScope(Dispatchers.IO).launch {
            if (pageViewModel.updateNote(
                    issueMaterial.title ?: return@launch,
                    issueMaterial.reason ?: return@launch
                )
            ) {
                curDialog?.dismiss()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.label_complete_delete_issue),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun onUpdateDismissClick() {
        curDialog?.dismiss()
    }
}
