package com.thinlineit.ctrlf.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.thinlineit.ctrlf.MainActivity
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.ActivityLoginBinding
import com.thinlineit.ctrlf.util.Status
import com.thinlineit.ctrlf.util.observeIfNotHandled

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_login)
    }
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
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

        binding.findId.setOnClickListener {
            Toast.makeText(this, R.string.notice_prepare, Toast.LENGTH_LONG).show()
        }

        binding.findPassword.setOnClickListener {
            Toast.makeText(this, R.string.notice_prepare, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}
