package com.thinlineit.ctrlf.registration.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.FragmentVerificationToFindPasswordBinding
import com.thinlineit.ctrlf.registration.RegistrationBaseFragment
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.Timer
import com.thinlineit.ctrlf.util.observeIfNotHandled
import com.thinlineit.ctrlf.util.setBackgroundById

class VerificationToFindPasswordFragment :
    RegistrationBaseFragment<FragmentVerificationToFindPasswordBinding>(
        R.layout.fragment_verification_to_find_password
    ) {
    private lateinit var navController: NavController
    private val viewModel by activityViewModels<FindPasswordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = this@VerificationToFindPasswordFragment.viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        viewModel.codeStatus.observeIfNotHandled(viewLifecycleOwner) {
            if (it == Status.FAILURE) {
                binding.findCode.setBackgroundById(R.drawable.background_round_red)
                binding.findCode.startAnimation(anim)
                binding.findCodeText.visibility = View.VISIBLE
            } else {
                navController.navigate(
                    R.id.action_verificationToFindPasswordFragment_to_passwordResetFragment
                )
                binding.findCodeText.visibility = View.GONE
            }
        }

        binding.backBtn.setOnClickListener {
            navController.navigate(
                R.id.action_verificationToFindPasswordFragment_to_emailVerifyFragment
            )
        }

        viewModel.countTimerStatus.observeIfNotHandled(viewLifecycleOwner) {
            if (it == Timer.FINISH) {
                binding.findCodeText.visibility = View.VISIBLE
                binding.findCodeText.text = requireContext().getString(R.string.notice_resend_code)
            } else {
                binding.findCodeText.visibility = View.GONE
                binding.findCode.setBackgroundById(R.drawable.background_round_white)
            }
        }
    }
}
