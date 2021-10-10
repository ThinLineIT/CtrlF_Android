package com.thinlineit.ctrlf.registration.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.FragmentPasswordResetBinding
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.base.RegistrationBaseFragment
import com.thinlineit.ctrlf.util.observeIfNotHandled

class PasswordResetFragment :
    RegistrationBaseFragment<FragmentPasswordResetBinding>(R.layout.fragment_password_reset) {
    private lateinit var navController: NavController
    private val viewModel by activityViewModels<FindPasswordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = this@PasswordResetFragment.viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        viewModel.passwordStatus.observeIfNotHandled(viewLifecycleOwner) {
            if (it == Status.FAILURE) {
                binding.findPasswordFind.setBackgroundById(R.drawable.background_round_red)
                binding.findPasswordFind.startAnimation(anim)
                binding.findPasswordText.visibility = View.VISIBLE
            } else {
                navController.navigate(
                    R.id.action_passwordResetFragment_to_passwordResetConfirmFragment
                )
                binding.findPasswordText.visibility = View.GONE
            }
        }

        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_passwordResetFragment_to_confirmBackToLoginFragment)
        }
    }
}
