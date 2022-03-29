package com.thinlineit.ctrlf.issue.detail

import android.content.Context
import android.widget.Toast
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.issue.IssueMaterial
import com.thinlineit.ctrlf.util.CustomDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IssueUpdateClickListener(
    private val context: Context,
    private val issueDetailViewModel: IssueDetailViewModel
) {
    private var curDialog: CustomDialog? = null

    fun onDefaultUpdateClick() {
        curDialog = CustomDialog(context).apply {
            title =
                context.resources.getString(issueDetailViewModel.toolbarTitle.value ?: return@apply)
            contentTitle = issueDetailViewModel.issueTitle.value
            contentBody = issueDetailViewModel.issue.value?.reason
            confirmButtonText = context.resources.getString(R.string.button_dialog_confirm)
            dismissButtonText = context.resources.getString(R.string.button_dialog_cancel)
            confirmClickListener = ::onDefaultUpdateConfirmClick
            dismissClickListener = ::onUpdateDismissClick
        }.also { it.show() }
    }

    fun onDeleteUpdateClick() {
        curDialog = CustomDialog(context).apply {
            title =
                context.resources.getString(issueDetailViewModel.toolbarTitle.value ?: return@apply)
            contentTitle = issueDetailViewModel.issueTitle.value
            contentBody = issueDetailViewModel.issue.value?.reason
            confirmButtonText = context.resources.getString(R.string.button_dialog_confirm)
            dismissButtonText = context.resources.getString(R.string.button_dialog_cancel)
            contentTitleEditable = true
            confirmClickListener = ::onDeleteUpdateConfirmClick
            dismissClickListener = ::onUpdateDismissClick
        }.also { it.show() }
    }

    private fun onDefaultUpdateConfirmClick(issueMaterial: IssueMaterial) {
        CoroutineScope(Dispatchers.IO).launch {
            if (issueDetailViewModel.updateIssue(
                    issueMaterial.title ?: return@launch,
                    issueMaterial.reason ?: return@launch
                )
            ) {
                curDialog?.dismiss()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.notice_update_success),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun onDeleteUpdateConfirmClick(issueMaterial: IssueMaterial) {
        CoroutineScope(Dispatchers.IO).launch {
            if (issueDetailViewModel.updateIssue(
                    null,
                    issueMaterial.reason ?: return@launch
                )
            ) {
                curDialog?.dismiss()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.notice_update_success),
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
