package com.thinlineit.ctrlf.page.editor

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.MimeTypeFilter
import androidx.fragment.app.activityViewModels
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.FragmentEditBinding
import com.thinlineit.ctrlf.util.base.BaseFragment
import com.thinlineit.ctrlf.util.copyUri
import kotlinx.android.synthetic.main.fragment_edit.markdownEdit

class PageEditFragment : BaseFragment<FragmentEditBinding>(R.layout.fragment_edit) {
    private val viewModel by activityViewModels<PageEditorViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.apply {
            viewModel = this@PageEditFragment.viewModel

            boldText.setOnClickListener { boldText() }
            headerText.setOnClickListener { headerText() }
            italicText.setOnClickListener { italicText() }
            quoteText.setOnClickListener { quoteText() }
            codeText.setOnClickListener { codeText() }
            bulletedList.setOnClickListener { bulletedList() }
            link.setOnClickListener { linkText() }
            numberList.setOnClickListener { numberList() }

            this@PageEditFragment.viewModel.url.observe(viewLifecycleOwner) {
                val cursorStart = markdownEdit.selectionStart
                markdownEdit.text.insert(cursorStart, it)
            }
        }
        ImageDropListener()
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

    // EditText drop action 관련 이슈 때문에 Button Layout에 드래그앤드롭 범위 지정
    private fun ImageDropListener() {
        binding.buttonLayout.setOnDragListener { view, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    val imageItem: ClipData.Item = event.clipData.getItemAt(0)
                    val uri = imageItem.uri
                    val mimeType = requireActivity().contentResolver.getType(uri) ?: null

                    if (MimeTypeFilter.matches(mimeType, IMAGE_MIME_TYPE) && mimeType != null) {
                        val imageName = uri.lastPathSegment
                        val dropPermissions =
                            ActivityCompat.requestDragAndDropPermissions(requireActivity(), event)
                        viewModel.loadImageUrl(
                            copyUri(
                                requireContext(),
                                uri,
                                imageName ?: "",
                                mimeType
                            )
                        )
                        dropPermissions?.release()
                    }
                    return@setOnDragListener true
                }
                else -> {
                    return@setOnDragListener true
                }
            }
        }
    }

    companion object {
        const val IMAGE_MIME_TYPE = "image/*"
    }
}
