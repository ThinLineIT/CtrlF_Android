package com.thinlineit.ctrlf.registration.signup

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
import com.thinlineit.ctrlf.databinding.FragmentNicknameBinding
import com.thinlineit.ctrlf.registration.RegistrationBaseFragment
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.observeIfNotHandled
import com.thinlineit.ctrlf.util.setBackgroundById
import kotlinx.android.synthetic.main.fragment_nickname.*

class EnterNicknameFragment :
    RegistrationBaseFragment<FragmentNicknameBinding>(R.layout.fragment_nickname) {
    private lateinit var navController: NavController
    private lateinit var callback: OnBackPressedCallback
    private val viewModel by activityViewModels<RegistrationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = this@EnterNicknameFragment.viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        viewModel.nicknameStatus.observeIfNotHandled(viewLifecycleOwner) {
            if (it == Status.FAILURE) {
                binding.regNickname.setBackgroundById(R.drawable.background_round_red)
                binding.regNickname.startAnimation(anim)
                binding.regNameText.visibility = View.VISIBLE
            } else {
                navController.navigate(
                    R.id.action_registerNicknameFragment_to_registerPasswordFragment
                )
                binding.regNameText.visibility = View.GONE
            }
        }

        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_registerNicknameFragment_to_registerBackFragment)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.navigate(R.id.action_registerNicknameFragment_to_registerBackFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}
