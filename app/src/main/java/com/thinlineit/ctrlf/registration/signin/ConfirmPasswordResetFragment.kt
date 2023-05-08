package com.thinlineit.ctrlf.registration.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.FragmentPasswordResetConfirmBinding
import com.thinlineit.ctrlf.registration.RegistrationBaseFragment
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.observeIfNotHandled
import com.thinlineit.ctrlf.util.setBackgroundById

class ConfirmPasswordResetFragment :
    RegistrationBaseFragment<FragmentPasswordResetConfirmBinding>(
        R.layout.fragment_password_reset_confirm
    ) {
    private lateinit var navController: NavController
    private val viewModel by activityViewModels<FindPasswordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = this@ConfirmPasswordResetFragment.viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        viewModel.passwordConfirmStatus.observeIfNotHandled(viewLifecycleOwner) {
            if (it == Status.FAILURE) {
                binding.confirmPasswordEditText.setBackgroundById(R.drawable.background_round_red)
                binding.confirmPasswordNoticeText.visibility = View.VISIBLE
            } else {
                binding.confirmPasswordEditText.setBackgroundById(R.drawable.background_round_white)
                binding.confirmPasswordNoticeText.visibility = View.GONE
            }
        }

        binding.backBtn.setOnClickListener {
            navController.navigate(
                R.id.action_passwordResetConfirmFragment_to_passwordResetFragment
            )
        }

        viewModel.completeClick.observeIfNotHandled(this) {
            navController.navigate(
                R.id.action_passwordResetConfirmFragment_to_completeFindPasswordFragment
            )
        }

        viewModel.passwordResetStatus.observe(viewLifecycleOwner) {
            binding.completeButton.isEnabled = it
        }
    }
}
