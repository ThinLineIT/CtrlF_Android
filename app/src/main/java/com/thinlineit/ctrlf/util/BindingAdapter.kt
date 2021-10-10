package com.thinlineit.ctrlf.util

import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.mukesh.MarkdownView
import com.thinlineit.ctrlf.registration.signin.FindPasswordViewModel
import com.thinlineit.ctrlf.registration.signup.RegistrationViewModel

@BindingAdapter("app:data")
fun <T> setRecyclerViewData(recyclerView: RecyclerView, data: T) {
    if (recyclerView.adapter is BindingRecyclerViewAdapter<*> && data != null) {
        (recyclerView.adapter as BindingRecyclerViewAdapter<T>).setData(data)
    }
}

@BindingAdapter("app:fragmentData")
fun <T> setFragmentStateData(viewPager: ViewPager2, data: T) {
    if (viewPager.adapter is BindingFragmentStateAdapter<*> && data != null)
        (viewPager.adapter as BindingFragmentStateAdapter<T>).setData(data)
}

@BindingAdapter("markdownString")
fun setMarkdownString(markdownView: MarkdownView, string: String?) {
    string?.let {
        markdownView.setMarkDownText(it)
    }
}

@BindingAdapter("app:addTextChangeListener")
fun addTextChangeListener(view: EditText, viewModel: ViewModel) {
    view.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            when (viewModel) {
                is RegistrationViewModel -> viewModel.checkPasswordSame()
                is FindPasswordViewModel -> viewModel.checkPasswordSame()
            }
        }
    })
}

@BindingAdapter("app:onEditorActionListener")
fun EditText.onEditorAction(callback: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            callback.invoke()
            return@setOnEditorActionListener true
        }
        false
    }
}
