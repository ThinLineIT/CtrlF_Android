package com.thinlineit.ctrlf.registration.signin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.FragmentPasswordResetBinding
import com.thinlineit.ctrlf.registration.RegistrationBaseFragment
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.observeIfNotHandled
import com.thinlineit.ctrlf.util.setBackgroundById

class PasswordResetFragment :
    RegistrationBaseFragment<FragmentPasswordResetBinding>(R.layout.fragment_password_reset) {
    private lateinit var navController: NavController
    private lateinit var callback: OnBackPressedCallback
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
                binding.newPasswordEditText.setBackgroundById(R.drawable.background_round_red)
                binding.newPasswordEditText.startAnimation(anim)
                binding.newPasswordText.visibility = View.VISIBLE
            } else {
                navController.navigate(
                    R.id.action_passwordResetFragment_to_passwordResetConfirmFragment
                )
                binding.newPasswordText.visibility = View.GONE
            }
        }

        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_passwordResetFragment_to_confirmBackToLoginFragment)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.navigate(
                    R.id.action_passwordResetFragment_to_confirmBackToLoginFragment
                )
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}
