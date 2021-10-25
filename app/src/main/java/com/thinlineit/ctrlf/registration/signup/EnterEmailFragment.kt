package com.thinlineit.ctrlf.registration.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.FragmentEmailBinding
import com.thinlineit.ctrlf.registration.RegistrationBaseFragment
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.observeIfNotHandled
import com.thinlineit.ctrlf.util.setBackgroundById

class EnterEmailFragment : RegistrationBaseFragment<FragmentEmailBinding>(R.layout.fragment_email) {
    private lateinit var navController: NavController
    private val viewModel by activityViewModels<RegistrationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = this@EnterEmailFragment.viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        viewModel.emailStatus.observeIfNotHandled(viewLifecycleOwner) {
            if (it == Status.FAILURE) {
                binding.regEmail.setBackgroundById(R.drawable.background_round_red)
                binding.regEmail.startAnimation(anim)
                binding.regEmailText.visibility = View.VISIBLE
            } else {
                navController.navigate(R.id.action_registerEmailFragment_to_registerCodeFragment)
                binding.regEmailText.visibility = View.GONE
            }
        }

        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_registerEmailFragment_to_loginActivity)
            requireActivity().finish()
        }
    }
}
