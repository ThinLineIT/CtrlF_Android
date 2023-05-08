package com.thinlineit.ctrlf.page.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.FragmentPreviewBinding
import com.thinlineit.ctrlf.util.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_preview.*

class PagePreviewFragment : BaseFragment<FragmentPreviewBinding>(R.layout.fragment_preview) {
    private val viewModel by activityViewModels<PageEditorViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = this@PagePreviewFragment.viewModel
        return binding.root
    }

    companion object {
        fun newInstance(): PagePreviewFragment {
            val args = Bundle()
            val fragment = PagePreviewFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
