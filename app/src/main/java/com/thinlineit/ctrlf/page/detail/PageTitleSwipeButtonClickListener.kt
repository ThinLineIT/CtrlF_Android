package com.thinlineit.ctrlf.page.detail

import android.content.Context
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.util.CustomDialog

class PageTitleSwipeButtonClickListener(
    private val context: Context,
    private val pageViewModel: PageViewModel
) : SwipeButtonClickListener {
    private var curDialog: CustomDialog? = null
    override fun onUpdate(pageId: Int) {
        TODO("Not yet implemented")
    }

    override fun onDelete(pageId: Int) {
        curDialog?.dismiss()
        curDialog = CustomDialog(context, pageId).apply {
            title = context.resources.getString(R.string.label_delete_page)
            contentBodyHint = context.resources.getString(R.string.hint_dialog_delete)
            confirmButtonText = context.resources.getString(R.string.button_dialog_confirm)
            dismissButtonText = context.resources.getString(R.string.button_dialog_cancel)
            confirmClickListener = ::onDeleteConfirmClick
            dismissClickListener = ::onDeleteDismissClick
        }
        curDialog?.show()
    }

    private fun onDeleteConfirmClick(id: Int, title: String, contentText: String) {
        pageViewModel.deletePage(contentText, id)
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
