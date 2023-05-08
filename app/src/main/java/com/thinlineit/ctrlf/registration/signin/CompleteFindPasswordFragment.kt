package com.thinlineit.ctrlf.registration.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.FragmentCompleteFindPasswordBinding
import com.thinlineit.ctrlf.util.base.BaseFragment

class CompleteFindPasswordFragment :
    BaseFragment<FragmentCompleteFindPasswordBinding>(R.layout.fragment_complete_find_password) {
    private lateinit var navController: NavController
    private val viewModel by activityViewModels<FindPasswordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = this@CompleteFindPasswordFragment.viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.loginButton.setOnClickListener {
            navController.navigate(
                R.id.action_completeFindPasswordFragment_to_loginActivity
            )
            requireActivity().finish()
        }
    }
}
