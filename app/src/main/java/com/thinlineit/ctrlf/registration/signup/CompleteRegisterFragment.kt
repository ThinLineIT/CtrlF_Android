package com.thinlineit.ctrlf.registration.signup

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.FragmentCompleteRegisterBinding
import com.thinlineit.ctrlf.util.base.BaseFragment

class CompleteRegisterFragment :
    BaseFragment<FragmentCompleteRegisterBinding>(R.layout.fragment_complete_register) {
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.loginButton.setOnClickListener {
            navController.navigate(R.id.action_completeRegisterFragment_to_loginActivity)
            requireActivity().finish()
        }
    }
}
