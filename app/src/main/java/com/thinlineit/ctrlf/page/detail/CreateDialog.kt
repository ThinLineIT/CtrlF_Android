package com.thinlineit.ctrlf.page.detail

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.thinlineit.ctrlf.databinding.FragmentCreateDialogBinding
import kotlinx.android.synthetic.main.fragment_create_dialog.cancelDialogButton
import kotlinx.android.synthetic.main.fragment_create_dialog.createDialogButton
import kotlinx.android.synthetic.main.fragment_create_dialog.createDialogReason
import kotlinx.android.synthetic.main.fragment_create_dialog.createDialogTitle

class CreateDialog(
    private val titleText: String,
    private val titleHint: String,
    private val onCreateClicked: (title: String, reason: String) -> Unit
) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCreateDialogBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            createDialog.text = titleText
            createDialogTitle.hint = titleHint
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createDialogButton.setOnClickListener {
            val title = createDialogTitle.text.toString()
            val reason = createDialogReason.text.toString()
            onCreateClicked(title, reason)
            dismiss()
        }
        cancelDialogButton.setOnClickListener {
            dismiss()
        }
    }
}
