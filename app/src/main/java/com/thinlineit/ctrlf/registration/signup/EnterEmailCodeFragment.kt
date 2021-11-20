package com.thinlineit.ctrlf.registration.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.FragmentCodeBinding
import com.thinlineit.ctrlf.registration.RegistrationBaseFragment
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.Timer
import com.thinlineit.ctrlf.util.observeIfNotHandled
import com.thinlineit.ctrlf.util.setBackgroundById
import kotlinx.android.synthetic.main.fragment_code.*

class EnterEmailCodeFragment :
    RegistrationBaseFragment<FragmentCodeBinding>(R.layout.fragment_code) {
    private lateinit var navController: NavController
    private val viewModel by activityViewModels<RegistrationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = this@EnterEmailCodeFragment.viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        viewModel.codeStatus.observeIfNotHandled(viewLifecycleOwner) {
            if (it == Status.FAILURE) {
                binding.regCode.setBackgroundById(R.drawable.background_round_red)
                binding.regCode.startAnimation(anim)
                binding.regCodeText.visibility = View.VISIBLE
            } else {
                navController.navigate(R.id.action_registerCodeFragment_to_registerNicknameFragment)
                binding.regCodeText.visibility = View.GONE
            }
        }

        viewModel.countTimerStatus.observeIfNotHandled(viewLifecycleOwner) {
            if (it == Timer.FINISH) {
                binding.regCodeText.visibility = View.VISIBLE
                binding.regCodeText.text = requireContext().getString(R.string.notice_resend_code)
            } else {
                binding.regCodeText.visibility = View.GONE
                binding.regCode.setBackgroundById(R.drawable.background_round_white)
            }
        }
        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_registerCodeFragment_to_registerEmailFragment)
        }
    }
}
