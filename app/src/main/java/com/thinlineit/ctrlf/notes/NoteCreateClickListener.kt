package com.thinlineit.ctrlf.notes

import android.content.Context
import android.widget.Toast
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.issue.IssueMaterial
import com.thinlineit.ctrlf.util.CustomDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteCreateClickListener(
    private val context: Context,
    private val notesViewModel: NotesViewModel
) {
    private var curDialog: CustomDialog? = null

    fun onCreateClick() {
        curDialog = CustomDialog(context).apply {
            title = context.resources.getString(R.string.label_add_note)
            contentTitleHint = context.resources.getString(R.string.hint_dialog_note_title_hint)
            contentBodyHint = context.resources.getString(R.string.hint_dialog_request_content)
            confirmButtonText = context.resources.getString(R.string.button_dialog_confirm)
            dismissButtonText = context.resources.getString(R.string.button_dialog_cancel)
            confirmClickListener = ::onCreateConfirmClick
            dismissClickListener = ::onDeleteDismissClick
        }
        curDialog?.show()
    }

    private fun onCreateConfirmClick(issueMaterial: IssueMaterial) {
        CoroutineScope(Dispatchers.IO).launch {
            if (notesViewModel.createNote(
                    issueMaterial.title ?: return@launch,
                    issueMaterial.reason ?: return@launch
                )
            ) {
                curDialog?.dismiss()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.notice_complete_create_note),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun onDeleteDismissClick() {
        curDialog?.dismiss()
    }
}
