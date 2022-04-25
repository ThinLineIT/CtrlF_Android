package com.thinlineit.ctrlf.registration.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.linecorp.lich.component.getComponent
import com.thinlineit.ctrlf.MainActivity
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.ActivityLoginBinding
import com.thinlineit.ctrlf.model.repository.UserRepository
import com.thinlineit.ctrlf.registration.signup.RegistrationActivity
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.observeIfNotHandled

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_login)
    }
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModel.LoginViewModelFactory(this.getComponent(UserRepository))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.loginStatus.observeIfNotHandled(this) {
            if (it == Status.SUCCESS) {
                MainActivity.start(this)
                finish()
            }
        }

        binding.registerButton.setOnClickListener {
            RegistrationActivity.start(this)
        }

        binding.findPassword.setOnClickListener {
            FindPasswordActivity.start(this)
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}
