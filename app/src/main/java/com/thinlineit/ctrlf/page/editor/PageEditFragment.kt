package com.thinlineit.ctrlf.page.editor

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.FragmentEditBinding
import com.thinlineit.ctrlf.util.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_edit.*

class PageEditFragment : BaseFragment<FragmentEditBinding>(R.layout.fragment_edit),
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
        TODO("Not yet implemented")
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
