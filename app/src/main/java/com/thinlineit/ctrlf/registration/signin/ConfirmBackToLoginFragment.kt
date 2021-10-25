package com.thinlineit.ctrlf.registration.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.FragmentConfirmBackToLoginBinding
import com.thinlineit.ctrlf.util.base.BaseFragment

class ConfirmBackToLoginFragment :
    BaseFragment<FragmentConfirmBackToLoginBinding>(R.layout.fragment_confirm_back_to_login) {
    private lateinit var navController: NavController
    private val viewModel by activityViewModels<FindPasswordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.apply {
            viewModel = this@ConfirmBackToLoginFragment.viewModel

            backBtn.setOnClickListener {
                navController.navigate(R.id.action_confirmBackToLoginFragment_to_loginActivity)
                requireActivity().finish()
            }
            cancelBtn.setOnClickListener {
                navController.navigate(
                    R.id.action_confirmBackToLoginFragment_to_passwordResetFragment
                )
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }
}
