package com.thinlineit.ctrlf.registration.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.linecorp.lich.component.getComponent
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.FragmentPasswordBinding
import com.thinlineit.ctrlf.model.repository.UserRepository
import com.thinlineit.ctrlf.registration.RegistrationBaseFragment
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.observeIfNotHandled
import com.thinlineit.ctrlf.util.setBackgroundById

class EnterPasswordFragment :
    RegistrationBaseFragment<FragmentPasswordBinding>(R.layout.fragment_password) {
    private lateinit var navController: NavController
    private val viewModel by activityViewModels<RegistrationViewModel> {
        RegistrationViewModel.RegistrationViewModelFactory(
            requireActivity().getComponent(UserRepository)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = this@EnterPasswordFragment.viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        viewModel.passwordStatus.observeIfNotHandled(viewLifecycleOwner) {
            if (it == Status.FAILURE) {
                binding.regPassword.setBackgroundById(R.drawable.background_round_red)
                binding.regPassword.startAnimation(anim)
                binding.regPasswordText.visibility = View.VISIBLE
            } else {
                navController.navigate(
                    R.id.action_registerPasswordFragment_to_registerConfirmPasswordFragment
                )
                binding.regPasswordText.visibility = View.GONE
            }
        }

        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_registerPasswordFragment_to_registerNicknameFragment)
        }
    }
}
