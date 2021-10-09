package com.thinlineit.ctrlf.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.thinlineit.ctrlf.MainActivity
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.login.LoginActivity
import com.thinlineit.ctrlf.repository.UserRepository
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        loadSplashView()
        startFirstActivity()
    }

    private fun loadSplashView() {
        Glide
            .with(this@SplashActivity)
            .asGif()
            .load(R.drawable.gif_splash_logo)
            .into(splashView)
    }

    private fun startFirstActivity() {
        CoroutineScope(Dispatchers.Default).launch {
            delay(2000L)
            if (userRepository.mayLogin()) {
                MainActivity.start(this@SplashActivity)
            } else {
                LoginActivity.start(this@SplashActivity)
            }
            finish()
        }
    }

    companion object {
        fun relaunch(context: Context) {
            val intent = Intent(context, SplashActivity::class.java)
            context.startActivity(intent)
        }
    }
}
