package com.thinlineit.ctrlf.registration.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.FragmentEmailVerifyBinding
import com.thinlineit.ctrlf.registration.RegistrationBaseFragment
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.observeIfNotHandled
import com.thinlineit.ctrlf.util.setBackgroundById

class EmailVerifyFragment :
    RegistrationBaseFragment<FragmentEmailVerifyBinding>(R.layout.fragment_email_verify) {
    private lateinit var navController: NavController
    private val viewModel by activityViewModels<FindPasswordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = this@EmailVerifyFragment.viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        viewModel.emailStatus.observeIfNotHandled(viewLifecycleOwner) {
            if (it == Status.FAILURE) {
                binding.emailEditText.setBackgroundById(R.drawable.background_round_red)
                binding.emailEditText.startAnimation(anim)
                binding.findPasswordEmailText.visibility = View.VISIBLE
            } else {
                navController.navigate(
                    R.id.action_emailVerifyFragment_to_verificationToFindPasswordFragment
                )
                binding.findPasswordEmailText.visibility = View.GONE
            }
        }

        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_emailVerifyFragment_to_loginActivity)
            requireActivity().finish()
        }
    }
}
