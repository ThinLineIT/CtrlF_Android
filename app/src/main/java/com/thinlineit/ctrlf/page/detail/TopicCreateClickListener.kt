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

class TopicCreateClickListener(
    private val context: Context,
    private val pageViewModel: PageViewModel
) {
    private var curDialog: CustomDialog? = null

    fun onCreateTopic() {
        curDialog = CustomDialog(context).apply {
            title = context.resources.getString(R.string.label_create_topic)
            contentTitleHint = context.resources.getString(R.string.hint_dialog_topic_title_hint)
            contentBodyHint = context.resources.getString(R.string.hint_dialog_content)
            confirmButtonText = context.resources.getString(R.string.button_dialog_confirm)
            dismissButtonText = context.resources.getString(R.string.button_dialog_cancel)
            confirmClickListener = ::onCreateConfirmClick
            dismissClickListener = ::onCreateDismissClick
        }
        curDialog?.show()
    }

    private fun onCreateConfirmClick(issueMaterial: IssueMaterial) {
        CoroutineScope(Dispatchers.IO).launch {
            if (pageViewModel.createTopic(
                    issueMaterial.title ?: return@launch,
                    issueMaterial.reason ?: return@launch
                )
            ) {
                curDialog?.dismiss()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.notice_complete_create_topic),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun onCreateDismissClick() {
        curDialog?.dismiss()
    }
}
