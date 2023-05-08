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

    private fun onDeleteConfirmClick(issueMaterial: IssueMaterial) {
        CoroutineScope(Dispatchers.IO).launch {
            if (pageViewModel.deletePage(
                    issueMaterial.reason ?: return@launch,
                    issueMaterial.contentId ?: return@launch
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

    private fun onDeleteDismissClick() {
        curDialog?.dismiss()
    }
}
