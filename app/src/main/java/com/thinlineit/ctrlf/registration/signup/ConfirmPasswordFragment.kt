package com.thinlineit.ctrlf.registration.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.FragmentConfirmPasswordBinding
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.base.BaseFragment
import com.thinlineit.ctrlf.util.observeIfNotHandled
import com.thinlineit.ctrlf.util.setBackgroundById

class ConfirmPasswordFragment :
    BaseFragment<FragmentConfirmPasswordBinding>(R.layout.fragment_confirm_password) {
    private lateinit var navController: NavController
    private val viewModel by activityViewModels<RegistrationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = this@ConfirmPasswordFragment.viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        viewModel.passwordConfirmStatus.observeIfNotHandled(viewLifecycleOwner) {
            if (it == Status.FAILURE) {
                binding.regPassword2.setBackgroundById(R.drawable.background_round_red)
                binding.regPasswordText.visibility = View.VISIBLE
            } else {
                binding.regPassword2.setBackgroundById(R.drawable.background_round_white)
                binding.regPasswordText.visibility = View.GONE
            }
        }

        viewModel.registerClick.observeIfNotHandled(this) {
            navController.navigate(
                R.id.action_registerConfirmPasswordFragment_to_completeRegisterFragment
            )
        }

        binding.backBtn.setOnClickListener {
            navController.navigate(
                R.id.action_registerConfirmPasswordFragment_to_registerPasswordFragment
            )
        }

        viewModel.registrationStatus.observe(viewLifecycleOwner) {
            binding.registerBtn.isEnabled = it
        }
    }
}
