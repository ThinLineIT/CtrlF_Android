package com.thinlineit.ctrlf.page.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.FragmentEditBinding
import com.thinlineit.ctrlf.util.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_edit.*

class PageEditFragment : BaseFragment<FragmentEditBinding>(R.layout.fragment_edit) {
    private val viewModel by activityViewModels<PageEditorViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = this@PageEditFragment.viewModel
        binding.apply {
            boldText.setOnClickListener { boldText() }
            headerText.setOnClickListener { headerText() }
            italicText.setOnClickListener { italicText() }
            quoteText.setOnClickListener { quoteText() }
            codeText.setOnClickListener { codeText() }
            bulletedList.setOnClickListener { bulletedList() }
            link.setOnClickListener { linkText() }
            numberList.setOnClickListener { numberList() }
        }
        return binding.root
    }

    fun boldText() {
        val boldStart = markdownEdit.selectionStart
        val boldEnd = markdownEdit.selectionEnd

        markdownEdit.text.insert(boldStart, getString(R.string.button_bold))
        markdownEdit.text.insert(boldEnd + 2, getString(R.string.button_bold))
    }

    fun headerText() {
        val headerStart = markdownEdit.selectionStart

        markdownEdit.text.insert(headerStart, getString(R.string.button_header))
    }

    fun italicText() {
        val italicStart = markdownEdit.selectionStart
        val italicEnd = markdownEdit.selectionEnd

        markdownEdit.text.insert(italicStart, getString(R.string.button_italic))
        markdownEdit.text.insert(italicEnd + 1, getString(R.string.button_italic))
    }

    fun quoteText() {
        val quoteStart = markdownEdit.selectionStart

        markdownEdit.text.insert(quoteStart, getString(R.string.button_quote))
    }

    fun codeText() {
        val codeStart = markdownEdit.selectionStart
        val codeEnd = markdownEdit.selectionEnd

        markdownEdit.text.insert(codeStart, getString(R.string.button_code_block))
        markdownEdit.text.insert(codeEnd + 3, getString(R.string.button_code_block))
    }

    fun linkText() {
        val linkStart = markdownEdit.selectionStart
        markdownEdit.text.insert(linkStart, getString(R.string.button_link))
    }

    fun bulletedList() {
        val bulletStart = markdownEdit.selectionStart
        markdownEdit.text.insert(bulletStart, getString(R.string.button_bulleted_list))
    }

    fun numberList() {
        val numberStart = markdownEdit.selectionStart
        markdownEdit.text.insert(numberStart, getString(R.string.button_number_list))
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
