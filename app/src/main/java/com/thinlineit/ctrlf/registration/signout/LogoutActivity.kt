package com.thinlineit.ctrlf.registration.signout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.linecorp.lich.component.getComponent
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.model.repository.UserRepository
import com.thinlineit.ctrlf.splash.SplashActivity
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
        val userRepository = this.getComponent(UserRepository)
        userRepository.logOut()
    }
}
