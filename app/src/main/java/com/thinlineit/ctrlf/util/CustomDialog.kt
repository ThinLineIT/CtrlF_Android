package com.thinlineit.ctrlf.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.issue.IssueMaterial
import kotlinx.android.synthetic.main.dialog_custom.*

class CustomDialog(
    context: Context,
    private val contentId: Int? = null
) : Dialog(context) {
    var title: String? = null
    var bodyText: String? = null
    var contentTitle: String? = null
    var contentBody: String? = null
    var contentTitleHint: String? = null
    var contentBodyHint: String? = null
    var confirmButtonText: String? = null
    var dismissButtonText: String? = null
    var contentTitleEditable: Boolean = false
    var confirmClickListener: ((IssueMaterial) -> Unit)? = null
    var dismissClickListener: (() -> Unit)? = null
    var buttonGravity = Gravity.END

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_custom)
        initView()
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun initView() {
        title?.let {
            dialogTitle.visibility = View.VISIBLE
            dialogTitle.text = it
        } ?: kotlin.run {
            dialogTitle.visibility = View.GONE
        }

        bodyText?.let {
            dialogBodyText.visibility = View.VISIBLE
            dialogBodyText.text = it
        } ?: kotlin.run {
            dialogBodyText.visibility = View.GONE
        }

        contentTitleHint?.let {
            contentTitleEditText.visibility = View.VISIBLE
            contentTitleEditText.hint = it
        } ?: kotlin.run {
            contentTitleEditText.visibility = View.GONE
        }

        contentBodyHint?.let {
            contentBodyEditText.visibility = View.VISIBLE
            contentBodyEditText.hint = it
        } ?: kotlin.run {
            contentBodyEditText.visibility = View.GONE
        }

        contentTitle?.let {
            contentTitleEditText.visibility = View.VISIBLE
            contentTitleEditText.setText(it)
            if (contentTitleEditable) {
                contentTitleEditText.apply {
                    isClickable = false
                    isFocusable = false
                }
            }
        } ?: kotlin.run {
            if (contentTitleHint.isNullOrEmpty())
                contentTitleEditText.visibility = View.GONE
        }

        contentBody?.let {
            contentBodyEditText.visibility = View.VISIBLE
            contentBodyEditText.setText(it)
        } ?: kotlin.run {
            if (contentBodyHint.isNullOrEmpty())
                contentBodyEditText.visibility = View.GONE
        }

        confirmButtonText?.let {
            confirmButton.text = it
        }

        dismissButtonText?.let {
            cancelButton.text = it
        }

        confirmClickListener?.let {
            confirmButton.visibility = View.VISIBLE
            confirmButton.setOnClickListener {
                val issueMaterial = IssueMaterial(
                    contentId,
                    contentTitleEditText.text.toString(),
                    contentBodyEditText.text.toString()
                )
                confirmClickListener?.invoke(
                    issueMaterial
                )
            }
        } ?: kotlin.run {
            confirmButton.visibility = View.GONE
        }

        dismissClickListener?.let {
            cancelButton.visibility = View.VISIBLE
            cancelButton.setOnClickListener {
                dismissClickListener?.invoke()
            }
        } ?: run {
            cancelButton.visibility = View.GONE
        }

        // page editor 에서 사용
//        when (buttonGravity) {
//            Gravity.CENTER -> {
//                val constraintSet= ConstraintSet()
//                constraintSet.clone(context, R.id.dialogChildLayout)
//                constraintSet.setHorizontalBias(R.id.cancelButton,0.5f)
//                // constraintSet.applyTo(confirmButton as ConstraintLayout)
//            }
//            Gravity.END -> {
//                val constraintSet= ConstraintSet()
//                constraintSet.clone(context, R.layout.dialog_custom)
//                constraintSet.setHorizontalBias(R.id.cancelButton,1f)
//                // constraintSet.applyTo(R.layout.dialog_custom)
//            }
//        }
    }

    enum class Gravity {
        CENTER, END
    }
}
