package com.thinlineit.ctrlf.registration.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.FragmentRegisterBackBinding
import com.thinlineit.ctrlf.util.base.BaseFragment

class ConfirmBackToEmailFragment :
    BaseFragment<FragmentRegisterBackBinding>(R.layout.fragment_register_back) {
    private lateinit var navController: NavController
    private val viewModel by activityViewModels<RegistrationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.apply {
            viewModel = this@ConfirmBackToEmailFragment.viewModel

            backBtn.setOnClickListener {
                navController.navigate(R.id.action_registerBackFragment_to_registerEmailFragment)
                this@ConfirmBackToEmailFragment.viewModel.resetEmailCodeValue()
            }
            cancelBtn.setOnClickListener {
                navController.navigate(R.id.action_registerBackFragment_to_registerNicknameFragment)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }
}
