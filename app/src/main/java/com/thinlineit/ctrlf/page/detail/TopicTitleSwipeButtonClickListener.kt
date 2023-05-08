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

class TopicTitleSwipeButtonClickListener(
    private val context: Context,
    private val pageViewModel: PageViewModel
) : SwipeButtonClickListener {
    private var curDialog: CustomDialog? = null

    override fun onUpdate(topicId: Int) {
        curDialog?.dismiss()
        curDialog = CustomDialog(context, topicId).apply {
            title = context.resources.getString(R.string.label_update_topic)
            contentTitleHint =
                context.resources.getString(R.string.hint_dialog_topic_title_hint_modify)
            contentBodyHint = context.resources.getString(R.string.hint_dialog_content)
            confirmButtonText = context.resources.getString(R.string.button_dialog_confirm)
            dismissButtonText = context.resources.getString(R.string.button_dialog_cancel)
            confirmClickListener = ::onUpdateConfirmClick
            dismissClickListener = ::onDismissClick
        }
        curDialog?.show()
    }

    override fun onDelete(topicId: Int) {
        curDialog?.dismiss()
        curDialog = CustomDialog(context, topicId).apply {
            title = context.resources.getString(R.string.label_delete_topic)
            contentBodyHint = context.resources.getString(R.string.hint_dialog_delete)
            confirmButtonText = context.resources.getString(R.string.button_dialog_confirm)
            dismissButtonText = context.resources.getString(R.string.button_dialog_cancel)
            confirmClickListener = ::onDeleteConfirmClick
            dismissClickListener = ::onDismissClick
        }
        curDialog?.show()
    }

    private fun onDeleteConfirmClick(issueMaterial: IssueMaterial) {
        CoroutineScope(Dispatchers.IO).launch {
            if (pageViewModel.deleteTopic(
                    issueMaterial.reason ?: return@launch,
                    issueMaterial.contentId ?: return@launch
                )
            ) {
                withContext(Dispatchers.Main) {
                    curDialog?.dismiss()
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.label_complete_delete_issue),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun onDismissClick() {
        curDialog?.dismiss()
    }

    private fun onUpdateConfirmClick(issueMaterial: IssueMaterial) {
        CoroutineScope(Dispatchers.IO).launch {
            if (pageViewModel.updateTopic(
                    issueMaterial.contentId ?: return@launch,
                    issueMaterial.title ?: return@launch,
                    issueMaterial.reason ?: return@launch,
                )
            ) {
                withContext(Dispatchers.Main) {
                    curDialog?.dismiss()
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.label_complete_delete_issue),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
