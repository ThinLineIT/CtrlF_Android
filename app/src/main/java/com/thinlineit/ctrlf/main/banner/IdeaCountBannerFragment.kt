package com.thinlineit.ctrlf.main.banner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.main.MainViewModel

class IdeaCountBannerFragment : Fragment() {

    private val viewModel = activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_idea_count_banner, container, false)
        // TODO: 후에 idea count api 로 변경해서 적용해야함.
        viewModel.value.issueList.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.ideaCount).text = it.size.toString()
        }
        return view
    }
}
