package com.thinlineit.ctrlf.page.detail

import android.content.Context
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.util.CustomDialog

class TopicTitleSwipeButtonClickListener(
    val context: Context,
    private val pageViewModel: PageViewModel
) : SwipeButtonClickListener {
    private var curDialog: CustomDialog? = null

    override fun onUpdate(topicId: Int) {
        context.let {
            CreateDialog(
                it,
                context.resources.getString(R.string.hint_dialog_topic_title_modify),
                context.resources.getString(R.string.hint_dialog_topic_title_hint_modify)
            ) { title, reason ->
                pageViewModel.updateTopic(
                    topicId,
                    title,
                    reason
                )
            }.openDialog()
        }
    }

    override fun onDelete(topicId: Int) {
        curDialog?.dismiss()
        curDialog = CustomDialog(context, topicId).apply {
            title = context.resources.getString(R.string.label_delete_topic)
            contentBodyHint = context.resources.getString(R.string.hint_dialog_delete)
            confirmButtonText = context.resources.getString(R.string.button_dialog_confirm)
            dismissButtonText = context.resources.getString(R.string.button_dialog_cancel)
            confirmClickListener = ::onDeleteConfirmClick
            dismissClickListener = ::onDeleteDismissClick
        }
        curDialog?.show()
    }

    private fun onDeleteConfirmClick(id: Int, title: String, contentText: String) {
        pageViewModel.deleteTopic(contentText, id)
        curDialog?.dismiss()
        curDialog = CustomDialog(context, id).apply {
            bodyText =
                context.resources.getString(R.string.label_complete_delete_issue)
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
