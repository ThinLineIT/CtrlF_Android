package com.thinlineit.ctrlf.page.editor

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.FragmentEditBinding
import com.thinlineit.ctrlf.util.base.BaseFragment
import java.io.File
import kotlinx.android.synthetic.main.fragment_edit.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PageEditFragment : BaseFragment<FragmentEditBinding>(R.layout.fragment_edit) {
    private val viewModel by activityViewModels<PageEditorViewModel>()

    @SuppressLint("ClickableViewAccessibility")
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
            image.setOnClickListener { imageText() }
            numberList.setOnClickListener { numberList() }
        }
        binding.markdownEdit.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            return@setOnTouchListener false
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

    fun imageText() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        getImage.launch(intent)
    }

    private val getImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val data = result.data
            if (data == null) {   // 어떤 이미지도 선택하지 않은 경우
            } else {   // 이미지를 하나라도 선택한 경우
                val clipData = data.clipData
                val imageUri = clipData!!.getItemAt(0).uri
/*
                val file = File(context?.cacheDir,System.currentTimeMillis().toString())
                file.outputStream().use {
                    activity?.contentResolver?.openInputStream(imageUri)?.copyTo(it)
                }
                file.outputStream().close()

                Log.d(TAG, "absolute path file : ${file.toURI()}")

                val fileFormDatarequestBody : RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file)
                val fileFormData : MultipartBody.Part = MultipartBody.Part.createFormData("ImageUploadRequestBody",file.name,fileFormDatarequestBody)
                viewModel.getImage(fileFormData)*/

                val linkStart = markdownEdit.selectionStart
                markdownEdit.text.insert(linkStart, getString(R.string.button_image_link_back))
                //markdownEdit.text.insert(linkStart, viewModel.imgUrl.value.toString()) 가운데에 URI
                markdownEdit.text.insert(linkStart, getString(R.string.button_image_link_front))
            }
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
