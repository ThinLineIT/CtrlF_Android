package com.thinlineit.ctrlf.page.editor

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.FragmentEditBinding
import com.thinlineit.ctrlf.util.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_edit.*

class PageEditFragment :
    BaseFragment<FragmentEditBinding>(R.layout.fragment_edit),
    ToolboxEventListener {
    private val viewModel by activityViewModels<PageEditorViewModel>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = this@PageEditFragment.viewModel
        binding.markdownEdit.setOnFocusChangeListener { v, hasFocus ->
            viewModel.toolboxController?.isActive = hasFocus
        }
        viewModel.toolboxController?.toolboxEventListener = this
        return binding.root
    }

    override fun boldText() {
        val boldStart = markdownEdit.selectionStart
        val boldEnd = markdownEdit.selectionEnd

        markdownEdit.text.insert(boldStart, getString(R.string.button_bold))
        markdownEdit.text.insert(boldEnd + 2, getString(R.string.button_bold))
    }

    override fun headerText() {
        val headerStart = markdownEdit.selectionStart

        markdownEdit.text.insert(headerStart, getString(R.string.button_header))
    }

    override fun italicText() {
        val italicStart = markdownEdit.selectionStart
        val italicEnd = markdownEdit.selectionEnd

        markdownEdit.text.insert(italicStart, getString(R.string.button_italic))
        markdownEdit.text.insert(italicEnd + 1, getString(R.string.button_italic))
    }

    override fun quoteText() {
        val quoteStart = markdownEdit.selectionStart

        markdownEdit.text.insert(quoteStart, getString(R.string.button_quote))
    }

    override fun codeText() {
        val codeStart = markdownEdit.selectionStart
        val codeEnd = markdownEdit.selectionEnd

        markdownEdit.text.insert(codeStart, getString(R.string.button_code_block))
        markdownEdit.text.insert(codeEnd + 3, getString(R.string.button_code_block))
    }

    override fun linkText() {
        val linkStart = markdownEdit.selectionStart
        markdownEdit.text.insert(linkStart, getString(R.string.button_link))
    }

    override fun bulletedList() {
        val bulletStart = markdownEdit.selectionStart
        markdownEdit.text.insert(bulletStart, getString(R.string.button_bulleted_list))
    }

    override fun numberList() {
        val numberStart = markdownEdit.selectionStart
        markdownEdit.text.insert(numberStart, getString(R.string.button_number_list))
    }

    override fun attachImage() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = MediaStore.Images.Media.CONTENT_TYPE
            data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            putExtra(Intent.ACTION_GET_CONTENT, true)
        }
        getImage.launch(intent)
    }

    private val getImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            val imageUri = result.data?.data ?: run {
                Toast.makeText(
                    activity,
                    getString(R.string.notice_no_img_selected),
                    Toast.LENGTH_LONG
                ).show()
                return@registerForActivityResult
            }

            // uri -> temp -> 파일 -> 폼데이터 과정 생략
            val linkStart = markdownEdit.selectionStart
            val linkUrl = String.format(getString(R.string.button_image_link_front), "$imageUri")
            markdownEdit.text.insert(
                linkStart,
                linkUrl
            )
        }

    companion object {
        fun newInstance(): PageEditFragment {
            val args = Bundle()
            val fragment = PageEditFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
