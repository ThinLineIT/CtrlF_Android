package com.thinlineit.ctrlf.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.ActivityCompleteRegisterBinding
import com.thinlineit.ctrlf.login.LoginActivity

class CompleteRegisterActivity : AppCompatActivity() {
    private val binding: ActivityCompleteRegisterBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_complete_register)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        binding.loginButton.setOnClickListener {
            LoginActivity.start(this)
            finish()
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CompleteRegisterActivity::class.java)
            context.startActivity(intent)
        }
    }
}
