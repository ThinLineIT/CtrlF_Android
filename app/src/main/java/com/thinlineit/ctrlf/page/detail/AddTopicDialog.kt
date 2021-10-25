package com.thinlineit.ctrlf.page.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.thinlineit.ctrlf.databinding.FragmentAddTopicDialogBinding
import kotlinx.android.synthetic.main.fragment_add_topic_dialog.*

class AddTopicDialog : DialogFragment() {

    private val pageViewModel by activityViewModels<PageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //false로 설정해 주면 화면밖 혹은 뒤로가기 버튼시 다이얼로그라 dismiss 되지 않는다.
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddTopicDialogBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            this.pageViewModel = this@AddTopicDialog.pageViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text = "Hello, Welcome to blackjin Tisotry"

        okButton.setOnClickListener {
            Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()

            val topicTitle = editTopicTitle.text.toString()
            val topicContent = editContent.text.toString()
            pageViewModel.complete(topicTitle,topicContent)
        }

        cancelButton.setOnClickListener {
            dismiss()
        }
    }

    fun getInstance(): AddTopicDialog {
        return AddTopicDialog()
    }

}
