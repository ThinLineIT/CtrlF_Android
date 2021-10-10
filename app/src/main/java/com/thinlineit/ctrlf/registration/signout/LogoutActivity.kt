package com.thinlineit.ctrlf.registration.signout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.splash.SplashActivity
import com.thinlineit.ctrlf.util.Application
import kotlinx.android.synthetic.main.activity_logout.cancelBtn
import kotlinx.android.synthetic.main.activity_logout.logoutBtn

class LogoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)
        logoutBtn.setOnClickListener {
            deleteInfo()
            finishAffinity()
            SplashActivity.relaunch(this)
        }
        cancelBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun deleteInfo() {
        Application.preferenceUtil.setString(EMAIL, "")
        Application.preferenceUtil.setString(PASSWORD, "")
        Application.preferenceUtil.setString(TOKEN, "")
    }

    companion object {
        private const val TOKEN = "token"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
    }
}
